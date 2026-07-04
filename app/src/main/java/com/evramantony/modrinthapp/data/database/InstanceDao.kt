package com.evramantony.modrinthapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinthapp.data.models.GameInstance

@Dao
interface InstanceDao {
    @Query("SELECT * FROM game_instances")
    suspend fun getAllInstances(): List<GameInstance>

    @Query("SELECT * FROM game_instances WHERE id = :id")
    suspend fun getInstanceById(id: Int): GameInstance?

    @Insert
    suspend fun insertInstance(instance: GameInstance): Long

    @Update
    suspend fun updateInstance(instance: GameInstance)

    @Delete
    suspend fun deleteInstance(instance: GameInstance)
}
