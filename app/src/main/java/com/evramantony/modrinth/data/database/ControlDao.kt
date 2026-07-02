package com.evramantony.modrinth.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinth.data.database.entity.ControlLayoutEntity
import com.evramantony.modrinth.data.database.entity.KeyBindingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ControlLayoutDao {
    @Insert
    suspend fun insertLayout(layout: ControlLayoutEntity)

    @Update
    suspend fun updateLayout(layout: ControlLayoutEntity)

    @Delete
    suspend fun deleteLayout(layout: ControlLayoutEntity)

    @Query("SELECT * FROM control_layouts WHERE id = :id")
    suspend fun getLayoutById(id: String): ControlLayoutEntity?

    @Query("SELECT * FROM control_layouts")
    fun getAllLayouts(): Flow<List<ControlLayoutEntity>>

    @Query("SELECT * FROM control_layouts WHERE isActive = 1 LIMIT 1")
    fun getActiveLayout(): Flow<ControlLayoutEntity?>

    @Query("UPDATE control_layouts SET isActive = 0")
    suspend fun deactivateAllLayouts()

    @Query("UPDATE control_layouts SET isActive = 1 WHERE id = :id")
    suspend fun setActiveLayout(id: String)

    @Query("SELECT * FROM control_layouts WHERE isCustom = 1")
    fun getCustomLayouts(): Flow<List<ControlLayoutEntity>>
}

@Dao
interface KeyBindingDao {
    @Insert
    suspend fun insertBinding(binding: KeyBindingEntity)

    @Update
    suspend fun updateBinding(binding: KeyBindingEntity)

    @Delete
    suspend fun deleteBinding(binding: KeyBindingEntity)

    @Query("SELECT * FROM key_bindings WHERE layoutId = :layoutId")
    suspend fun getBindingsForLayout(layoutId: String): List<KeyBindingEntity>

    @Query("SELECT * FROM key_bindings WHERE layoutId = :layoutId AND action = :action")
    suspend fun getBindingForAction(layoutId: String, action: String): KeyBindingEntity?

    @Query("DELETE FROM key_bindings WHERE layoutId = :layoutId")
    suspend fun deleteBindingsForLayout(layoutId: String)
}
