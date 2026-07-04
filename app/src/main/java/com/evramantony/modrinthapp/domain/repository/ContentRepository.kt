package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.ContentSource
import com.evramantony.modrinthapp.data.models.ContentType
import com.evramantony.modrinthapp.data.models.InstalledContent
import com.evramantony.modrinthapp.data.models.ModDependency

interface ContentRepository {
    suspend fun getAllContentByInstance(instanceId: Int): List<InstalledContent>
    suspend fun getContentByType(instanceId: Int, type: ContentType): List<InstalledContent>
    suspend fun installContent(
        instanceId: Int,
        contentId: String,
        name: String,
        type: ContentType,
        source: ContentSource,
        version: String,
        filePath: String,
        fileSize: Long,
        sha1: String
    ): Long

    suspend fun uninstallContent(contentId: String, instanceId: Int): Boolean
    suspend fun toggleContent(contentId: String, instanceId: Int, enabled: Boolean): Boolean
    suspend fun addDependency(modId: String, dependencyModId: String, dependencyType: String, versionRange: String): Long
    suspend fun getDependencies(modId: String): List<ModDependency>
    suspend fun exportContent(instanceId: Int, exportPath: String, contentType: ContentType? = null): Boolean
    suspend fun importContent(instanceId: Int, importPath: String, type: ContentType, source: ContentSource): List<Long>
}
