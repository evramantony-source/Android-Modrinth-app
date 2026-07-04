package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.MinecraftVersion
import com.evramantony.modrinthapp.data.models.VersionType

interface MinecraftRepository {
    suspend fun getAllVersions(): List<MinecraftVersion>
    suspend fun getVersionsByType(type: VersionType): List<MinecraftVersion>
    suspend fun getVersionByName(name: String): MinecraftVersion?
    suspend fun downloadVersion(version: MinecraftVersion): Boolean
}
