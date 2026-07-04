package com.evramantony.modrinthapp.domain.launcher

import com.evramantony.modrinthapp.data.models.LauncherSettings

interface SettingsManager {
    suspend fun getSettings(): LauncherSettings
    suspend fun updateSettings(settings: LauncherSettings)
    suspend fun resetToDefaults()
    suspend fun exportSettings(exportPath: String): Boolean
    suspend fun importSettings(importPath: String): Boolean
}
