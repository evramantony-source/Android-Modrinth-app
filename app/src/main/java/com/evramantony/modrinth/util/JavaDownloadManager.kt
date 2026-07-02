package com.evramantony.modrinth.util

import android.content.Context
import android.os.Build
import com.evramantony.modrinth.data.model.GraphicsRenderer
import com.evramantony.modrinth.data.model.JavaVersion
import java.io.File

class JavaDownloadManager(private val context: Context) {
    
    companion object {
        // Official Java builds for Android (using portable distributions)
        private const val JAVA_8_URL = "https://cdn.azul.com/zulu/bin/zulu8.80.0.21-ca-jdk8.0.402-linux_x64.tar.gz"
        private const val JAVA_17_URL = "https://cdn.azul.com/zulu/bin/zulu17.54.19-ca-jdk17.0.11-linux_x64.tar.gz"
        private const val JAVA_21_URL = "https://cdn.azul.com/zulu/bin/zulu21.36.17-ca-jdk21.0.3-linux_x64.tar.gz"
        private const val JAVA_25_URL = "https://cdn.azul.com/zulu/bin/zulu25.2.9-ca-jdk25.0.2-linux_x64.tar.gz"
        
        private val JAVA_VERSIONS = mapOf(
            JavaVersion.JAVA_8 to "8.0.402",
            JavaVersion.JAVA_17 to "17.0.11",
            JavaVersion.JAVA_21 to "21.0.3",
            JavaVersion.JAVA_25 to "25.0.2"
        )
    }
    
    fun getDownloadUrlForJavaVersion(version: JavaVersion): String {
        return when (version) {
            JavaVersion.JAVA_8 -> JAVA_8_URL
            JavaVersion.JAVA_17 -> JAVA_17_URL
            JavaVersion.JAVA_21 -> JAVA_21_URL
            JavaVersion.JAVA_25 -> JAVA_25_URL
        }
    }
    
    fun getJavaInstallationPath(javaVersion: JavaVersion): File {
        val baseDir = File(context.getExternalFilesDir(null), "java_runtimes")
        return File(baseDir, "java-${javaVersion.majorVersion}")
    }
    
    fun getJavaBinaryPath(javaVersion: JavaVersion): File {
        val installPath = getJavaInstallationPath(javaVersion)
        return File(installPath, "bin/java")
    }
    
    fun isJavaInstalled(javaVersion: JavaVersion): Boolean {
        return getJavaBinaryPath(javaVersion).exists()
    }
    
    fun getRequiredJavaForMinecraftVersion(mcVersion: String): JavaVersion {
        return when {
            mcVersion.startsWith("26.") || mcVersion.startsWith("1.26") -> JavaVersion.JAVA_25
            mcVersion.startsWith("1.21") || mcVersion.startsWith("23.") || mcVersion.startsWith("24.") -> JavaVersion.JAVA_21
            mcVersion.startsWith("1.17") || mcVersion.startsWith("1.18") || mcVersion.startsWith("1.19") || mcVersion.startsWith("1.20") -> JavaVersion.JAVA_17
            else -> JavaVersion.JAVA_8
        }
    }
}

class RendererOptimizer {
    
    fun getJVMArgsForRenderer(
        renderer: GraphicsRenderer,
        maxMemoryMB: Int = 2048
    ): List<String> {
        val baseArgs = mutableListOf(
            "-Xmx${maxMemoryMB}M",
            "-Xms512M",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=200",
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:G1NewCollectionPercentage=30",
            "-XX:G1MaxNewGenPercent=40",
            "-XX:InitiatingHeapOccupancyPercent=35",
            "-XX:+DisableExplicitGC",
            "-XX:+AlwaysPreTouch",
            "-XX:+ParallelRefProcEnabled",
            "-Dlog4j.configurationFile=log4j2.xml"
        )
        
        when (renderer) {
            GraphicsRenderer.VULKAN -> {
                baseArgs.add("-Dforge.enabledModLoaders=javafxmod")
                baseArgs.add("-Dorg.lwjgl.opengl.Window.undecorated=true")
            }
            GraphicsRenderer.ZINK -> {
                baseArgs.add("-Dorg.lwjgl.opengl.Window.undecorated=true")
                baseArgs.add("-Dorg.lwjgl.util.noui=true")
            }
            GraphicsRenderer.OPENGL_ES_3 -> {
                baseArgs.add("-XX:+UseStringDeduplication")
            }
            GraphicsRenderer.OPENGL_ES_2 -> {
                baseArgs.add("-Xms256M")
                baseArgs.add("-XX:MaxGCPauseMillis=100")
            }
            GraphicsRenderer.OPENGL_4 -> {
                baseArgs.add("-XX:+UseStringDeduplication")
                baseArgs.add("-XX:CompileThreshold=10000")
            }
            GraphicsRenderer.SOFTWARE -> {
                baseArgs.add("-Xms128M")
                baseArgs.add("-XX:MaxGCPauseMillis=50")
            }
        }
        
        return baseArgs
    }
    
    fun getGameArgsForRenderer(
        renderer: GraphicsRenderer,
        width: Int = 854,
        height: Int = 480
    ): List<String> {
        val args = mutableListOf(
            "--width", width.toString(),
            "--height", height.toString()
        )
        
        when (renderer) {
            GraphicsRenderer.VULKAN -> {
                args.add("--setVariable")
                args.add("graphicsEngine=vulkan")
            }
            GraphicsRenderer.ZINK -> {
                args.add("--setVariable")
                args.add("graphicsEngine=zink")
            }
            GraphicsRenderer.OPENGL_4, GraphicsRenderer.OPENGL_ES_3 -> {
                args.add("--setVariable")
                args.add("graphicsQuality=fast")
            }
            GraphicsRenderer.OPENGL_ES_2 -> {
                args.add("--setVariable")
                args.add("graphicsQuality=low")
            }
            else -> {}
        }
        
        return args
    }
}
