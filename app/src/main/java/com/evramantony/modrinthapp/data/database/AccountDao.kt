package com.evramantony.modrinthapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinthapp.data.models.GameAccount

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<GameAccount>

    @Query("SELECT * FROM accounts WHERE isActive = 1")
    suspend fun getActiveAccount(): GameAccount?

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): GameAccount?

    @Insert
    suspend fun insertAccount(account: GameAccount): Long

    @Update
    suspend fun updateAccount(account: GameAccount)

    @Delete
    suspend fun deleteAccount(account: GameAccount)

    @Query("UPDATE accounts SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAllAccounts()
}
