package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.GameAccount

interface AccountRepository {
    suspend fun getAllAccounts(): List<GameAccount>
    suspend fun getActiveAccount(): GameAccount?
    suspend fun createOfflineAccount(username: String): GameAccount
    suspend fun createOnlineAccount(username: String, password: String): GameAccount?
    suspend fun deleteAccount(accountId: Int)
    suspend fun setActiveAccount(accountId: Int)
    suspend fun refreshToken(accountId: Int): Boolean
}
