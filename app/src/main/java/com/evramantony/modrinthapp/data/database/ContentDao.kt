package com.evramantony.modrinthapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.evramantony.modrinthapp.data.models.InstalledContent

@Dao
interface ContentDao {
    @Query("SELECT * FROM installed_content WHERE instanceId = :instanceId")
    suspend fun getContentByInstance(instanceId: Int): List<InstalledContent>

    @Query("SELECT * FROM installed_content WHERE id = :id")
    suspend fun getContentById(id: Int): InstalledContent?

    @Query("SELECT * FROM installed_content WHERE contentId = :contentId AND instanceId = :instanceId")
    suspend fun getContentByIdAndInstance(contentId: String, instanceId: Int): InstalledContent?

    @Insert
    suspend fun insertContent(content: InstalledContent): Long

    @Update
    suspend fun updateContent(content: InstalledContent)

    @Delete
    suspend fun deleteContent(content: InstalledContent)

    @Query("DELETE FROM installed_content WHERE instanceId = :instanceId")
    suspend fun deleteContentByInstance(instanceId: Int)
}
