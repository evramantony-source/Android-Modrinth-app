package com.evramantony.modrinthapp.presentation.launcher

import android.content.Context
import android.util.Log
import com.evramantony.modrinthapp.domain.launcher.GameOutputMonitor
import com.evramantony.modrinthapp.domain.launcher.CrashReportData
import java.io.BufferedReader
import java.io.InputStreamReader

class GameOutputMonitorImpl(
    private val context: Context
) : GameOutputMonitor {

    private var monitorThread: Thread? = null
    private var isMonitoring = false
    private var lastCrashReport: CrashReportData? = null

    override fun startMonitoring(processId: Int, instanceId: Int) {
        isMonitoring = true
        monitorThread = Thread {
            try {
                val proc = Runtime.getRuntime().exec(arrayOf("logcat", "-c"))
                proc.waitFor()

                val reader = BufferedReader(InputStreamReader(
                    Runtime.getRuntime().exec("logcat").inputStream
                ))
                var line: String?
                var errorBuffer = mutableListOf<String>()
                var hasError = false
                var errorType = ""
                var errorMessage = ""

                while (isMonitoring && reader.readLine().also { line = it } != null) {
                    Log.d("GameMonitor", line!!)

                    when {
                        line!!.contains("OutOfMemoryError", ignoreCase = true) -> {
                            hasError = true
                            errorType = "OutOfMemoryError"
                            errorMessage = "Ran out of memory. Increase RAM in settings."
                            errorBuffer.add(line!!)
                        }
                        line!!.contains("NullPointerException", ignoreCase = true) -> {
                            hasError = true
                            errorType = "NullPointerException"
                            errorMessage = "Null pointer exception - likely mod conflict."
                            errorBuffer.add(line!!)
                        }
                        line!!.contains("NoClassDefFoundError", ignoreCase = true) -> {
                            hasError = true
                            errorType = "NoClassDefFoundError"
                            errorMessage = "Missing class definition - check mod dependencies."
                            errorBuffer.add(line!!)
                        }
                        line!!.contains("ClassNotFoundException", ignoreCase = true) -> {
                            hasError = true
                            errorType = "ClassNotFoundException"
                            errorMessage = "Mod version incompatible with game version."
                            errorBuffer.add(line!!)
                        }
                        line!!.contains("Exception", ignoreCase = true) ||
                        line!!.contains("Error", ignoreCase = true) -> {
                            errorBuffer.add(line!!)
                            if (errorBuffer.size > 50) {
                                errorBuffer.removeAt(0)
                            }
                        }
                    }
                }

                if (hasError) {
                    lastCrashReport = CrashReportData(
                        hasError = true,
                        errorMessage = errorMessage,
                        stackTrace = errorBuffer.joinToString("\n"),
                        affectedMods = extractAffectedMods(errorBuffer),
                        exceptionType = errorType
                    )
                }
            } catch (e: Exception) {
                Log.e("GameMonitor", "Error monitoring game output", e)
            }
        }
        monitorThread?.start()
    }

    override fun stopMonitoring() {
        isMonitoring = false
        monitorThread?.join(1000)
    }

    override fun getCrashReport(): CrashReportData? {
        return lastCrashReport
    }

    private fun extractAffectedMods(logLines: List<String>): List<String> {
        val affectedMods = mutableListOf<String>()
        logLines.forEach { line ->
            val modPatterns = listOf(
                "mod",
                "fabric",
                "forge",
                "quilt",
                "plugin"
            )
            modPatterns.forEach { pattern ->
                if (line.contains(pattern, ignoreCase = true)) {
                    val words = line.split(" ")
                    words.forEach { word ->
                        if (word.contains(pattern, ignoreCase = true)) {
                            affectedMods.add(word)
                        }
                    }
                }
            }
        }
        return affectedMods.distinct()
    }
}
