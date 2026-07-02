package com.evramantony.modrinth.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinth.data.database.entity.JavaRuntimeEntity
import com.evramantony.modrinth.data.database.entity.RendererPreferenceEntity
import com.evramantony.modrinth.data.database.entity.DeviceGraphicsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JavaRuntimeDao {
    @Insert
    suspend fun insertRuntime(runtime: JavaRuntimeEntity)

    @Update
    suspend fun updateRuntime(runtime: JavaRuntimeEntity)

    @Delete
    suspend fun deleteRuntime(runtime: JavaRuntimeEntity)

    @Query("SELECT * FROM java_runtimes WHERE version = :version")
    suspend fun getRuntimeByVersion(version: String): JavaRuntimeEntity?

    @Query("SELECT * FROM java_runtimes WHERE majorVersion = :majorVersion")
    suspend fun getRuntimeByMajorVersion(majorVersion: Int): JavaRuntimeEntity?

    @Query("SELECT * FROM java_runtimes ORDER BY majorVersion DESC")
    fun getAllRuntimes(): Flow<List<JavaRuntimeEntity>>

    @Query("SELECT * FROM java_runtimes WHERE isInstalled = 1 ORDER BY majorVersion DESC")
    fun getInstalledRuntimes(): Flow<List<JavaRuntimeEntity>>
}

@Dao
interface RendererPreferenceDao {
    @Insert
    suspend fun insertPreference(preference: RendererPreferenceEntity)

    @Update
    suspend fun updatePreference(preference: RendererPreferenceEntity)

    @Delete
    suspend fun deletePreference(preference: RendererPreferenceEntity)

    @Query("SELECT * FROM renderer_preferences WHERE versionId = :versionId")
    suspend fun getPreferenceForVersion(versionId: String): RendererPreferenceEntity?

    @Query("SELECT * FROM renderer_preferences")
    fun getAllPreferences(): Flow<List<RendererPreferenceEntity>>
}

@Dao
interface DeviceGraphicsDao {
    @Insert
    suspend fun insertGraphicsInfo(info: DeviceGraphicsEntity)

    @Update
    suspend fun updateGraphicsInfo(info: DeviceGraphicsEntity)

    @Query("SELECT * FROM device_graphics LIMIT 1")
    suspend fun getGraphicsInfo(): DeviceGraphicsEntity?

    @Query("SELECT * FROM device_graphics LIMIT 1")
    fun getGraphicsInfoFlow(): Flow<DeviceGraphicsEntity?>
}
