package com.evramantony.modrinthapp.data.repository

import com.evramantony.modrinthapp.data.database.CrashLogDao
import com.evramantony.modrinthapp.data.models.CrashAnalysis
import com.evramantony.modrinthapp.data.models.CrashLog
import com.evramantony.modrinthapp.domain.repository.CrashLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CrashLogRepositoryImpl(
    private val crashLogDao: CrashLogDao
) : CrashLogRepository {

    override suspend fun logCrash(
        instanceId: Int,
        gameVersion: String,
        crashMessage: String,
        errorType: String,
        stackTrace: String,
        relevantMods: List<String>,
        javaVersion: String
    ): Long {
        return withContext(Dispatchers.IO) {
            val crash = CrashLog(
                instanceId = instanceId,
                gameVersion = gameVersion,
                crashMessage = crashMessage,
                errorType = errorType,
                stackTrace = stackTrace,
                relevantMods = relevantMods.joinToString(","),
                javaVersion = javaVersion
            )
            crashLogDao.insertCrash(crash)
        }
    }

    override suspend fun analyzeCrash(crashId: Int): CrashAnalysis? {
        return withContext(Dispatchers.IO) {
            val crash = crashLogDao.getCrashById(crashId) ?: return@withContext null
            analyzeCrashInternal(crash)
        }
    }

    override suspend fun getRecentCrashes(limit: Int): List<CrashLog> {
        return withContext(Dispatchers.IO) {
            crashLogDao.getRecentCrashes(limit)
        }
    }

    override suspend fun getCrashesForInstance(instanceId: Int): List<CrashLog> {
        return withContext(Dispatchers.IO) {
            crashLogDao.getCrashesByInstance(instanceId)
        }
    }

    private fun analyzeCrashInternal(crash: CrashLog): CrashAnalysis {
        val affectedMods = crash.relevantMods.split(",").filter { it.isNotEmpty() }
        val suggestions = mutableListOf<String>()
        val primaryReason = when {
            crash.stackTrace.contains("OutOfMemoryError") -> {
                suggestions.add("Increase allocated RAM in launcher settings")
                suggestions.add("Remove unnecessary mods")
                "Ran out of memory"
            }
            crash.stackTrace.contains("NoClassDefFoundError") -> {
                suggestions.add("A required mod may not be properly installed")
                suggestions.add("Check mod dependencies")
                "Missing class definition (likely mod conflict)"
            }
            crash.stackTrace.contains("ClassNotFoundException") -> {
                suggestions.add("Mod version incompatible with game version")
                suggestions.add("Update or downgrade the problematic mod")
                "Class not found (mod version mismatch)"
            }
            crash.stackTrace.contains("NullPointerException") -> {
                suggestions.add("Remove recently installed mods one by one to identify culprit")
                suggestions.add("Check for mod conflicts")
                "Null pointer exception (likely mod conflict)"
            }
            crash.stackTrace.contains("java.lang.RuntimeException") -> {
                suggestions.add("Check mod configurations")
                suggestions.add("Update mods to latest versions")
                "Runtime exception"
            }
            crash.stackTrace.contains("ValidationException") -> {
                suggestions.add("Verify game file integrity")
                suggestions.add("Reinstall problematic mods")
                "Game validation failed"
            }
            crash.errorType.contains("SIGSEGV", ignoreCase = true) ||
            crash.errorType.contains("SIGABRT", ignoreCase = true) -> {
                suggestions.add("Native crash - likely renderer issue")
                suggestions.add("Try switching to OpenGL ES renderer")
                suggestions.add("Update graphics drivers")
                "Native crash (signal received)"
            }
            crash.crashMessage.contains("Unable to start the X server", ignoreCase = true) -> {
                suggestions.add("Renderer not supported on this device")
                suggestions.add("Try OPENGL_ES renderer")
                "Graphics server error"
            }
            crash.stackTrace.contains("StackOverflowError") -> {
                suggestions.add("Mod creates recursive calls")
                suggestions.add("Disable recently added mods")
                "Stack overflow"
            }
            else -> {
                suggestions.add("Unknown crash - check full stack trace")
                suggestions.add("Try removing recently added mods")
                suggestions.add("Update Java runtime")
                crash.crashMessage.takeIf { it.isNotEmpty() } ?: "Unknown error"
            }
        }

        val similarCrashes = emptyList<CrashLog>()  // Can implement fuzzy matching later

        return CrashAnalysis(
            isCrashAnalyzed = true,
            primaryReason = primaryReason,
            affectedMods = affectedMods,
            suggestions = suggestions,
            similarCrashes = similarCrashes
        )
    }
}
