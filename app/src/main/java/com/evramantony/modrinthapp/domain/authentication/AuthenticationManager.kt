package com.evramantony.modrinthapp.domain.authentication

import com.evramantony.modrinthapp.data.models.GameAccount

interface AuthenticationManager {
    suspend fun authenticateOffline(username: String): GameAccount?
    suspend fun authenticateOnline(username: String, password: String): GameAccount?
    suspend fun refreshAuthentication(account: GameAccount): Boolean
    suspend fun logout(account: GameAccount): Boolean
    fun generateOfflineUUID(username: String): String
    fun generateClientToken(): String
}
