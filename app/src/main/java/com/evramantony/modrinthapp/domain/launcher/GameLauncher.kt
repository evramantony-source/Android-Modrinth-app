package com.evramantony.modrinthapp.domain.launcher

import com.evramantony.modrinthapp.data.models.GameInstance
import com.evramantony.modrinthapp.data.models.GameAccount

interface GameLauncher {
    suspend fun launch(
        instance: GameInstance,
        account: GameAccount,
        renderer: GameRenderer = GameRenderer.OPENGL_ES
    ): GameLaunchResult

    suspend fun stop(): Boolean
    fun isRunning(): Boolean
    fun getGameProcess(): Process?
}

data class GameLaunchResult(
    val success: Boolean,
    val processId: Int = 0,
    val errorMessage: String = ""
)

enum class GameRenderer {
    OPENGL_ES,
    VULKAN,
    MOBILE_GLUES
}
