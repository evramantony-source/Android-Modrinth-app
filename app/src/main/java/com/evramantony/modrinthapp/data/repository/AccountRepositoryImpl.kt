package com.evramantony.modrinthapp.data.repository

import com.evramantony.modrinthapp.data.database.AccountDao
import com.evramantony.modrinthapp.data.models.GameAccount
import com.evramantony.modrinthapp.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun getAllAccounts(): List<GameAccount> {
        return withContext(Dispatchers.IO) {
            accountDao.getAllAccounts()
        }
    }

    override suspend fun getActiveAccount(): GameAccount? {
        return withContext(Dispatchers.IO) {
            accountDao.getActiveAccount()
        }
    }

    override suspend fun createOfflineAccount(username: String): GameAccount {
        return withContext(Dispatchers.IO) {
            val uuid = generateOfflineUUID(username)
            val account = GameAccount(
                username = username,
                isOffline = true,
                uuid = uuid,
                clientToken = UUID.randomUUID().toString()
            )
            accountDao.insertAccount(account)
            account
        }
    }

    override suspend fun createOnlineAccount(username: String, password: String): GameAccount? {
        return withContext(Dispatchers.IO) {
            // Implement Microsoft/Mojang authentication here
            null
        }
    }

    override suspend fun deleteAccount(accountId: Int) {
        return withContext(Dispatchers.IO) {
            val account = accountDao.getAccountById(accountId) ?: return@withContext
            accountDao.deleteAccount(account)
        }
    }

    override suspend fun setActiveAccount(accountId: Int) {
        return withContext(Dispatchers.IO) {
            accountDao.deactivateAllAccounts()
            val account = accountDao.getAccountById(accountId) ?: return@withContext
            accountDao.updateAccount(account.copy(isActive = true, lastUsedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun refreshToken(accountId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            // Implement token refresh here
            true
        }
    }

    private fun generateOfflineUUID(username: String): String {
        return UUID.nameUUIDFromBytes("OfflineMode:$username".toByteArray()).toString()
    }
}
