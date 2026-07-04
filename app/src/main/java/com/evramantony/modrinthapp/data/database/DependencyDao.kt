package com.evramantony.modrinthapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinthapp.data.models.ModDependency

@Dao
interface DependencyDao {
    @Query("SELECT * FROM mod_dependencies WHERE modId = :modId")
    suspend fun getDependenciesForMod(modId: String): List<ModDependency>

    @Query("SELECT * FROM mod_dependencies WHERE dependencyModId = :modId")
    suspend fun getModsThatDependOn(modId: String): List<ModDependency>

    @Insert
    suspend fun insertDependency(dependency: ModDependency): Long

    @Update
    suspend fun updateDependency(dependency: ModDependency)

    @Delete
    suspend fun deleteDependency(dependency: ModDependency)

    @Query("DELETE FROM mod_dependencies WHERE modId = :modId")
    suspend fun deleteDependenciesForMod(modId: String)
}
