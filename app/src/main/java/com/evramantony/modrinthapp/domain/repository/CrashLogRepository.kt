package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.CrashAnalysis
import com.evramantony.modrinthapp.data.models.CrashLog

interface CrashLogRepository {
    suspend fun logCrash(
        instanceId: Int,
        gameVersion: String,
        crashMessage: String,
        errorType: String,
        stackTrace: String,
        relevantMods: List<String>,
        javaVersion: String
    ): Long

    suspend fun analyzeCrash(crashId: Int): CrashAnalysis?
    suspend fun getRecentCrashes(limit: Int = 50): List<CrashLog>
    suspend fun getCrashesForInstance(instanceId: Int): List<CrashLog>
}
