package com.evramantony.modrinthapp.domain.usecase

import com.evramantony.modrinthapp.data.models.ContentSource
import com.evramantony.modrinthapp.data.models.ContentType
import com.evramantony.modrinthapp.domain.repository.ContentRepository

class ImportExportUseCase(
    private val contentRepository: ContentRepository
) {
    suspend fun exportInstance(
        instanceId: Int,
        exportPath: String
    ): Boolean {
        return contentRepository.exportContent(
            instanceId = instanceId,
            exportPath = exportPath,
            contentType = null
        )
    }

    suspend fun exportContentType(
        instanceId: Int,
        exportPath: String,
        contentType: ContentType
    ): Boolean {
        return contentRepository.exportContent(
            instanceId = instanceId,
            exportPath = exportPath,
            contentType = contentType
        )
    }

    suspend fun importMods(
        instanceId: Int,
        importPath: String,
        source: ContentSource = ContentSource.LOCAL
    ): List<Long> {
        return contentRepository.importContent(
            instanceId = instanceId,
            importPath = importPath,
            type = ContentType.MOD,
            source = source
        )
    }

    suspend fun importModpack(
        instanceId: Int,
        importPath: String,
        source: ContentSource = ContentSource.LOCAL
    ): List<Long> {
        return contentRepository.importContent(
            instanceId = instanceId,
            importPath = importPath,
            type = ContentType.MODPACK,
            source = source
        )
    }

    suspend fun importResourcePacks(
        instanceId: Int,
        importPath: String
    ): List<Long> {
        return contentRepository.importContent(
            instanceId = instanceId,
            importPath = importPath,
            type = ContentType.RESOURCE_PACK,
            source = ContentSource.LOCAL
        )
    }

    suspend fun importShaderPacks(
        instanceId: Int,
        importPath: String
    ): List<Long> {
        return contentRepository.importContent(
            instanceId = instanceId,
            importPath = importPath,
            type = ContentType.SHADER_PACK,
            source = ContentSource.LOCAL
        )
    }

    suspend fun importDataPacks(
        instanceId: Int,
        importPath: String
    ): List<Long> {
        return contentRepository.importContent(
            instanceId = instanceId,
            importPath = importPath,
            type = ContentType.DATA_PACK,
            source = ContentSource.LOCAL
        )
    }
}
