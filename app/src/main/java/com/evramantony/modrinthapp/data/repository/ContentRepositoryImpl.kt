package com.evramantony.modrinthapp.data.repository

import com.evramantony.modrinthapp.data.database.ContentDao
import com.evramantony.modrinthapp.data.database.DependencyDao
import com.evramantony.modrinthapp.data.models.ContentSource
import com.evramantony.modrinthapp.data.models.ContentType
import com.evramantony.modrinthapp.data.models.InstalledContent
import com.evramantony.modrinthapp.data.models.ModDependency
import com.evramantony.modrinthapp.domain.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ContentRepositoryImpl(
    private val contentDao: ContentDao,
    private val dependencyDao: DependencyDao
) : ContentRepository {

    override suspend fun getAllContentByInstance(instanceId: Int): List<InstalledContent> {
        return withContext(Dispatchers.IO) {
            contentDao.getContentByInstance(instanceId)
        }
    }

    override suspend fun getContentByType(instanceId: Int, type: ContentType): List<InstalledContent> {
        return withContext(Dispatchers.IO) {
            contentDao.getContentByInstance(instanceId).filter { it.type == type }
        }
    }

    override suspend fun installContent(
        instanceId: Int,
        contentId: String,
        name: String,
        type: ContentType,
        source: ContentSource,
        version: String,
        filePath: String,
        fileSize: Long,
        sha1: String
    ): Long {
        return withContext(Dispatchers.IO) {
            val content = InstalledContent(
                instanceId = instanceId,
                contentId = contentId,
                name = name,
                type = type,
                source = source,
                version = version,
                filePath = filePath,
                fileSize = fileSize,
                sha1 = sha1
            )
            contentDao.insertContent(content)
        }
    }

    override suspend fun uninstallContent(contentId: String, instanceId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val content = contentDao.getContentByIdAndInstance(contentId, instanceId) ?: return@withContext false
            try {
                File(content.filePath).delete()
                contentDao.deleteContent(content)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun toggleContent(contentId: String, instanceId: Int, enabled: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val content = contentDao.getContentByIdAndInstance(contentId, instanceId) ?: return@withContext false
            contentDao.updateContent(content.copy(enabled = enabled))
            true
        }
    }

    override suspend fun addDependency(
        modId: String,
        dependencyModId: String,
        dependencyType: String,
        versionRange: String
    ): Long {
        return withContext(Dispatchers.IO) {
            val dependency = ModDependency(
                modId = modId,
                dependencyModId = dependencyModId,
                dependencyType = when (dependencyType.lowercase()) {
                    "required" -> com.evramantony.modrinthapp.data.models.DependencyType.REQUIRED
                    "optional" -> com.evramantony.modrinthapp.data.models.DependencyType.OPTIONAL
                    "incompatible" -> com.evramantony.modrinthapp.data.models.DependencyType.INCOMPATIBLE
                    else -> com.evramantony.modrinthapp.data.models.DependencyType.EMBEDDED
                },
                versionRange = versionRange
            )
            dependencyDao.insertDependency(dependency)
        }
    }

    override suspend fun getDependencies(modId: String): List<ModDependency> {
        return withContext(Dispatchers.IO) {
            dependencyDao.getDependenciesForMod(modId)
        }
    }

    override suspend fun exportContent(
        instanceId: Int,
        exportPath: String,
        contentType: ContentType?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val contents = if (contentType != null) {
                    getContentByType(instanceId, contentType)
                } else {
                    getAllContentByInstance(instanceId)
                }

                val exportDir = File(exportPath)
                exportDir.mkdirs()

                contents.forEach { content ->
                    val sourceFile = File(content.filePath)
                    if (sourceFile.exists()) {
                        sourceFile.copyTo(
                            File(exportDir, sourceFile.name),
                            overwrite = true
                        )
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun importContent(
        instanceId: Int,
        importPath: String,
        type: ContentType,
        source: ContentSource
    ): List<Long> {
        return withContext(Dispatchers.IO) {
            val results = mutableListOf<Long>()
            val importDir = File(importPath)

            if (importDir.isDirectory) {
                importDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.extension in listOf("jar", "zip")) {
                        try {
                            val id = installContent(
                                instanceId = instanceId,
                                contentId = file.nameWithoutExtension,
                                name = file.nameWithoutExtension,
                                type = type,
                                source = source,
                                version = "1.0",
                                filePath = file.absolutePath,
                                fileSize = file.length(),
                                sha1 = ""
                            )
                            results.add(id)
                        } catch (e: Exception) {
                            // Log error
                        }
                    }
                }
            }
            results
        }
    }
}
