package com.evramantony.modrinthapp.domain.launcher

import com.evramantony.modrinthapp.data.models.GameInstance
import com.evramantony.modrinthapp.data.models.GameAccount

interface GameLauncher {
    suspend fun launch(
        instance: GameInstance,
        account: GameAccount,
        renderer: GameRenderer = GameRenderer.OPENGL_ES
    ): Boolean

    suspend fun stop(): Boolean
    fun isRunning(): Boolean
    fun getGameProcess(): Process?
}

enum class GameRenderer {
    OPENGL_ES,
    VULKAN,
    MOBILE_GLUES
}
