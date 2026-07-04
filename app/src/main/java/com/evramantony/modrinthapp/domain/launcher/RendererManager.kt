package com.evramantony.modrinthapp.domain.launcher

interface RendererManager {
    fun initializeRenderer(renderer: GameRenderer): Boolean
    fun setGraphicsSettings(settings: GraphicsSettings): Boolean
    fun switchRenderer(from: GameRenderer, to: GameRenderer): Boolean
    fun getAvailableRenderers(): List<GameRenderer>
    fun cleanup()
}

data class GraphicsSettings(
    val resolution: Pair<Int, Int> = 1080 to 1920,
    val refreshRate: Int = 60,
    val msaa: Int = 0,
    val shadowDistance: Int = 8,
    val renderDistance: Int = 12,
    val brightness: Float = 1.0f,
    val contrast: Float = 1.0f
)
