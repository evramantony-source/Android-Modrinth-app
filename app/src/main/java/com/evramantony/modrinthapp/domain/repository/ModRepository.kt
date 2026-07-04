package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.Mod
import com.evramantony.modrinthapp.data.models.ModFile

interface ModRepository {
    suspend fun searchMods(query: String, limit: Int = 20, offset: Int = 0): List<Mod>
    suspend fun getModDetails(modId: String): Mod?
    suspend fun getModVersions(modId: String, gameVersion: String, loader: String): List<ModFile>
    suspend fun downloadMod(mod: ModFile, destination: String): Boolean
    suspend fun installMod(mod: ModFile, instanceId: Int): Boolean
    suspend fun uninstallMod(modId: String, instanceId: Int): Boolean
}
