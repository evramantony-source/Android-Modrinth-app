package com.evramantony.modrinth.data.model

import com.google.gson.annotations.SerializedName

data class JavaRuntime(
    val version: String,
    val majorVersion: Int,
    val downloadUrl: String,
    val size: Long,
    val sha256: String,
    val isInstalled: Boolean = false,
    val installPath: String? = null
)

enum class JavaVersion(val versionName: String, val majorVersion: Int) {
    JAVA_8("Java 8", 8),
    JAVA_17("Java 17", 17),
    JAVA_21("Java 21", 21),
    JAVA_25("Java 25", 25)
}

enum class GraphicsRenderer {
    OPENGL_ES_2,      // OpenGL ES 2.0 (older devices)
    OPENGL_ES_3,      // OpenGL ES 3.0+ (modern devices)
    OPENGL_4,         // OpenGL 4.x (desktop emulators)
    VULKAN,           // Vulkan (high-performance)
    ZINK,             // Zink (OpenGL on Vulkan)
    SOFTWARE          // Software rendering (fallback)
}

data class RendererInfo(
    val renderer: GraphicsRenderer,
    val glVersion: String?,
    val vendor: String?,
    val extensions: List<String>,
    val maxTextureSize: Int,
    val maxRenderSize: Int,
    val supportsInstancing: Boolean,
    val supportsVAO: Boolean,
    val supportsShaderStorage: Boolean
)

data class DeviceGraphicsInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: Int,
    val primaryRenderer: GraphicsRenderer,
    val supportedRenderers: List<GraphicsRenderer>,
    val rendererInfo: RendererInfo,
    val recommendedJavaVersion: JavaVersion,
    val recommendedRenderer: GraphicsRenderer
)
