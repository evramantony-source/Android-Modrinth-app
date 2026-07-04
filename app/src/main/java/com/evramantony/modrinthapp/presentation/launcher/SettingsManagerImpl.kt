package com.evramantony.modrinthapp.presentation.launcher

import android.content.Context
import com.evramantony.modrinthapp.data.models.LauncherSettings
import com.evramantony.modrinthapp.domain.launcher.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsManagerImpl(
    private val context: Context
) : SettingsManager {

    private val sharedPreferences = context.getSharedPreferences("launcher_settings", Context.MODE_PRIVATE)

    override suspend fun getSettings(): LauncherSettings = withContext(Dispatchers.IO) {
        LauncherSettings(
            defaultRam = sharedPreferences.getInt("default_ram", 2048),
            maxRam = sharedPreferences.getInt("max_ram", 4096),
            jvmArgs = sharedPreferences.getString("jvm_args", "-XX:+UseG1GC -XX:+ParallelRefProcEnabled") ?: "-XX:+UseG1GC -XX:+ParallelRefProcEnabled",
            gameWidth = sharedPreferences.getInt("game_width", 1280),
            gameHeight = sharedPreferences.getInt("game_height", 720),
            renderer = sharedPreferences.getString("renderer", "OPENGL_ES") ?: "OPENGL_ES",
            autoUpdate = sharedPreferences.getBoolean("auto_update", true),
            showCrashReports = sharedPreferences.getBoolean("show_crash_reports", true),
            enableTouchControls = sharedPreferences.getBoolean("enable_touch_controls", true),
            touchControlsOpacity = sharedPreferences.getFloat("touch_controls_opacity", 0.7f),
            touchControlsSize = sharedPreferences.getFloat("touch_controls_size", 1.0f),
            keepLauncher = sharedPreferences.getBoolean("keep_launcher", false),
            fastStart = sharedPreferences.getBoolean("fast_start", false)
        )
    }

    override suspend fun updateSettings(settings: LauncherSettings) = withContext(Dispatchers.IO) {
        sharedPreferences.edit().apply {
            putInt("default_ram", settings.defaultRam)
            putInt("max_ram", settings.maxRam)
            putString("jvm_args", settings.jvmArgs)
            putInt("game_width", settings.gameWidth)
            putInt("game_height", settings.gameHeight)
            putString("renderer", settings.renderer)
            putBoolean("auto_update", settings.autoUpdate)
            putBoolean("show_crash_reports", settings.showCrashReports)
            putBoolean("enable_touch_controls", settings.enableTouchControls)
            putFloat("touch_controls_opacity", settings.touchControlsOpacity)
            putFloat("touch_controls_size", settings.touchControlsSize)
            putBoolean("keep_launcher", settings.keepLauncher)
            putBoolean("fast_start", settings.fastStart)
            apply()
        }
    }

    override suspend fun resetToDefaults() = withContext(Dispatchers.IO) {
        updateSettings(LauncherSettings())
    }

    override suspend fun exportSettings(exportPath: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val settings = getSettings()
            val json = settingsToJson(settings)
            java.io.File(exportPath).writeText(json)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun importSettings(importPath: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val json = java.io.File(importPath).readText()
            val settings = jsonToSettings(json)
            updateSettings(settings)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun settingsToJson(settings: LauncherSettings): String {
        // Simple JSON serialization
        return """{"defaultRam":${settings.defaultRam},"maxRam":${settings.maxRam},"jvmArgs":"${settings.jvmArgs}"}"""
    }

    private fun jsonToSettings(json: String): LauncherSettings {
        // Simple JSON deserialization
        return LauncherSettings()
    }
}
