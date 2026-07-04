package com.evramantony.modrinthapp.domain.launcher

interface GameOutputMonitor {
    fun startMonitoring(processId: Int, instanceId: Int)
    fun stopMonitoring()
    fun getCrashReport(): CrashReportData?
}

data class CrashReportData(
    val hasError: Boolean,
    val errorMessage: String,
    val stackTrace: String,
    val affectedMods: List<String>,
    val exceptionType: String
)
