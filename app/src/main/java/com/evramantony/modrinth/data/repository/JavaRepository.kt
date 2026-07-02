package com.evramantony.modrinth.data.repository

import android.content.Context
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.EGL14
import android.os.Build
import com.evramantony.modrinth.data.database.JavaRuntimeDao
import com.evramantony.modrinth.data.database.RendererPreferenceDao
import com.evramantony.modrinth.data.database.DeviceGraphicsDao
import com.evramantony.modrinth.data.database.entity.DeviceGraphicsEntity
import com.evramantony.modrinth.data.database.entity.JavaRuntimeEntity
import com.evramantony.modrinth.data.database.entity.RendererPreferenceEntity
import com.evramantony.modrinth.data.model.DeviceGraphicsInfo
import com.evramantony.modrinth.data.model.GraphicsRenderer
import com.evramantony.modrinth.data.model.JavaVersion
import com.evramantony.modrinth.data.model.RendererInfo
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.microedition.khronos.opengles.GL10

class JavaRuntimeRepository(
    private val javaDao: JavaRuntimeDao,
    private val rendererDao: RendererPreferenceDao,
    private val deviceGraphicsDao: DeviceGraphicsDao,
    private val context: Context
) {
    
    fun getAllRuntimes(): Flow<List<JavaRuntimeEntity>> = javaDao.getAllRuntimes()
    
    fun getInstalledRuntimes(): Flow<List<JavaRuntimeEntity>> = javaDao.getInstalledRuntimes()
    
    suspend fun getRuntimeByVersion(majorVersion: Int): JavaRuntimeEntity? {
        return javaDao.getRuntimeByMajorVersion(majorVersion)
    }
    
    suspend fun registerJavaRuntime(
        version: String,
        majorVersion: Int,
        downloadUrl: String,
        size: Long,
        sha256: String
    ): JavaRuntimeEntity {
        val runtime = JavaRuntimeEntity(
            version = version,
            majorVersion = majorVersion,
            downloadUrl = downloadUrl,
            size = size,
            sha256 = sha256,
            isInstalled = false
        )
        javaDao.insertRuntime(runtime)
        return runtime
    }
    
    suspend fun markJavaAsInstalled(
        majorVersion: Int,
        installPath: String
    ) {
        val runtime = javaDao.getRuntimeByMajorVersion(majorVersion)
        if (runtime != null) {
            javaDao.updateRuntime(
                runtime.copy(
                    isInstalled = true,
                    installPath = installPath,
                    installDate = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun getJavaInstallDirectory(): File {
        return File(context.getExternalFilesDir(null), "java_runtimes")
    }
    
    suspend fun detectDeviceGraphicsCapabilities(): DeviceGraphicsInfo {
        val renderer = detectPrimaryRenderer()
        val supportedRenderers = detectSupportedRenderers()
        val rendererInfo = getDetailedRendererInfo()
        val recommendedJava = recommendJavaVersion()
        val recommendedRenderer = recommendRenderer(supportedRenderers)
        
        val graphicsInfo = DeviceGraphicsInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.SDK_INT,
            primaryRenderer = renderer,
            supportedRenderers = supportedRenderers,
            rendererInfo = rendererInfo,
            recommendedJavaVersion = recommendedJava,
            recommendedRenderer = recommendedRenderer
        )
        
        // Save to database
        val entity = DeviceGraphicsEntity(
            manufacturer = graphicsInfo.manufacturer,
            model = graphicsInfo.model,
            androidVersion = graphicsInfo.androidVersion,
            primaryRenderer = graphicsInfo.primaryRenderer.toString(),
            supportedRenderers = Gson().toJson(graphicsInfo.supportedRenderers.map { it.toString() }),
            glVersion = rendererInfo.glVersion,
            vendor = rendererInfo.vendor,
            maxTextureSize = rendererInfo.maxTextureSize,
            maxRenderSize = rendererInfo.maxRenderSize,
            detectionDate = System.currentTimeMillis()
        )
        deviceGraphicsDao.insertGraphicsInfo(entity)
        
        return graphicsInfo
    }
    
    private fun detectPrimaryRenderer(): GraphicsRenderer {
        return try {
            val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
            EGL14.eglInitialize(display, intArrayOf(1), 0, intArrayOf(1), 0)
            
            val configs = arrayOfNulls<android.opengl.EGLConfig>(1)
            val numConfigs = intArrayOf(1)
            EGL14.eglChooseConfig(
                display,
                intArrayOf(EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, EGL14.EGL_NONE),
                configs,
                1,
                numConfigs
            )
            
            val context = EGL14.eglCreateContext(
                display,
                configs[0],
                EGL14.EGL_NO_CONTEXT,
                intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE),
                0
            )
            
            val surface = EGL14.eglCreatePbufferSurface(
                display,
                configs[0],
                intArrayOf(EGL14.EGL_WIDTH, 64, EGL14.EGL_HEIGHT, 64, EGL14.EGL_NONE),
                0
            )
            
            EGL14.eglMakeCurrent(display, surface, surface, context)
            
            val glString = android.opengl.GLES20.glGetString(GL10.GL_RENDERER)
            
            EGL14.eglMakeCurrent(
                display,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroyContext(display, context)
            EGL14.eglDestroySurface(display, surface)
            EGL14.eglTerminate(display)
            
            when {
                glString?.contains("Vulkan", ignoreCase = true) == true -> GraphicsRenderer.VULKAN
                glString?.contains("Zink", ignoreCase = true) == true -> GraphicsRenderer.ZINK
                Build.VERSION.SDK_INT >= 21 -> GraphicsRenderer.OPENGL_ES_3
                else -> GraphicsRenderer.OPENGL_ES_2
            }
        } catch (e: Exception) {
            GraphicsRenderer.OPENGL_ES_2 // Fallback
        }
    }
    
    private fun detectSupportedRenderers(): List<GraphicsRenderer> {
        val supported = mutableListOf<GraphicsRenderer>()
        supported.add(GraphicsRenderer.OPENGL_ES_2)
        
        if (Build.VERSION.SDK_INT >= 18) {
            supported.add(GraphicsRenderer.OPENGL_ES_3)
        }
        
        if (Build.VERSION.SDK_INT >= 24) {
            supported.add(GraphicsRenderer.VULKAN)
        }
        
        if (Build.VERSION.SDK_INT >= 26) {
            supported.add(GraphicsRenderer.ZINK)
        }
        
        supported.add(GraphicsRenderer.SOFTWARE)
        
        return supported
    }
    
    private fun getDetailedRendererInfo(): RendererInfo {
        return try {
            val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
            EGL14.eglInitialize(display, intArrayOf(1), 0, intArrayOf(1), 0)
            
            val configs = arrayOfNulls<android.opengl.EGLConfig>(1)
            val numConfigs = intArrayOf(1)
            EGL14.eglChooseConfig(
                display,
                intArrayOf(EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, EGL14.EGL_NONE),
                configs,
                1,
                numConfigs
            )
            
            val context = EGL14.eglCreateContext(
                display,
                configs[0],
                EGL14.EGL_NO_CONTEXT,
                intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE),
                0
            )
            
            val surface = EGL14.eglCreatePbufferSurface(
                display,
                configs[0],
                intArrayOf(EGL14.EGL_WIDTH, 64, EGL14.EGL_HEIGHT, 64, EGL14.EGL_NONE),
                0
            )
            
            EGL14.eglMakeCurrent(display, surface, surface, context)
            
            val glVersion = android.opengl.GLES20.glGetString(GL10.GL_VERSION)
            val vendor = android.opengl.GLES20.glGetString(GL10.GL_VENDOR)
            val renderer = android.opengl.GLES20.glGetString(GL10.GL_RENDERER)
            val extensions = android.opengl.GLES20.glGetString(GL10.GL_EXTENSIONS)?.split(" ") ?: emptyList()
            
            val maxTexSize = IntArray(1)
            android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_TEXTURE_SIZE, maxTexSize, 0)
            
            val maxRenderSize = IntArray(2)
            android.opengl.GLES20.glGetIntegerv(android.opengl.GLES20.GL_MAX_RENDERBUFFER_SIZE, maxRenderSize, 0)
            
            EGL14.eglMakeCurrent(
                display,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroyContext(display, context)
            EGL14.eglDestroySurface(display, surface)
            EGL14.eglTerminate(display)
            
            RendererInfo(
                renderer = detectPrimaryRenderer(),
                glVersion = glVersion,
                vendor = vendor,
                extensions = extensions,
                maxTextureSize = maxTexSize[0],
                maxRenderSize = maxRenderSize[0],
                supportsInstancing = extensions.contains("GL_EXT_instanced_arrays"),
                supportsVAO = extensions.contains("GL_OES_vertex_array_object"),
                supportsShaderStorage = extensions.contains("GL_ARB_shader_storage_buffer_object")
            )
        } catch (e: Exception) {
            RendererInfo(
                renderer = GraphicsRenderer.SOFTWARE,
                glVersion = null,
                vendor = null,
                extensions = emptyList(),
                maxTextureSize = 2048,
                maxRenderSize = 2048,
                supportsInstancing = false,
                supportsVAO = false,
                supportsShaderStorage = false
            )
        }
    }
    
    private fun recommendJavaVersion(): JavaVersion {
        return when {
            Build.VERSION.SDK_INT >= 34 -> JavaVersion.JAVA_25 // Android 15+
            Build.VERSION.SDK_INT >= 33 -> JavaVersion.JAVA_21 // Android 13+
            Build.VERSION.SDK_INT >= 31 -> JavaVersion.JAVA_17 // Android 12+
            else -> JavaVersion.JAVA_8
        }
    }
    
    private fun recommendRenderer(supported: List<GraphicsRenderer>): GraphicsRenderer {
        return when {
            supported.contains(GraphicsRenderer.VULKAN) && Build.VERSION.SDK_INT >= 24 -> GraphicsRenderer.VULKAN
            supported.contains(GraphicsRenderer.ZINK) && Build.VERSION.SDK_INT >= 26 -> GraphicsRenderer.ZINK
            supported.contains(GraphicsRenderer.OPENGL_ES_3) -> GraphicsRenderer.OPENGL_ES_3
            else -> GraphicsRenderer.OPENGL_ES_2
        }
    }
    
    suspend fun setRendererPreference(
        versionId: String,
        renderer: GraphicsRenderer,
        maxFPS: Int = 60,
        textureQuality: String = "medium",
        renderDistance: Int = 8
    ) {
        val preference = RendererPreferenceEntity(
            versionId = versionId,
            preferredRenderer = renderer.toString(),
            maxFPS = maxFPS,
            textureQuality = textureQuality,
            renderDistance = renderDistance
        )
        rendererDao.insertPreference(preference)
    }
    
    suspend fun getRendererPreference(versionId: String): RendererPreferenceEntity? {
        return rendererDao.getPreferenceForVersion(versionId)
    }
}
