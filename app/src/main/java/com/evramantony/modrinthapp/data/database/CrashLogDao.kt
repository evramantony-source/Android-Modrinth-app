package com.evramantony.modrinthapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinthapp.data.models.CrashLog

@Dao
interface CrashLogDao {
    @Query("SELECT * FROM crash_logs ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentCrashes(limit: Int = 50): List<CrashLog>

    @Query("SELECT * FROM crash_logs WHERE id = :id")
    suspend fun getCrashById(id: Int): CrashLog?

    @Query("SELECT * FROM crash_logs WHERE instanceId = :instanceId ORDER BY timestamp DESC")
    suspend fun getCrashesByInstance(instanceId: Int): List<CrashLog>

    @Insert
    suspend fun insertCrash(crash: CrashLog): Long

    @Delete
    suspend fun deleteCrash(crash: CrashLog)

    @Query("DELETE FROM crash_logs WHERE timestamp < :olderThan")
    suspend fun deleteOldCrashes(olderThan: Long)
}
