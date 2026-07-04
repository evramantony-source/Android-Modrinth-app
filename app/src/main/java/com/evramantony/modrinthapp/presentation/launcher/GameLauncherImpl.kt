package com.evramantony.modrinthapp.presentation.launcher

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.evramantony.modrinthapp.data.models.GameInstance
import com.evramantony.modrinthapp.data.models.GameAccount
import com.evramantony.modrinthapp.domain.launcher.GameLauncher
import com.evramantony.modrinthapp.domain.launcher.GameLaunchResult
import com.evramantony.modrinthapp.domain.launcher.GameRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class GameLauncherImpl(
    private val context: Context
) : GameLauncher {

    private var gameProcess: Process? = null
    private var processId: Int = 0

    override suspend fun launch(
        instance: GameInstance,
        account: GameAccount,
        renderer: GameRenderer
    ): GameLaunchResult = withContext(Dispatchers.Default) {
        try {
            val javaPath = getJavaPath(instance.javaVersion.version)
            if (!File(javaPath).exists()) {
                return@withContext GameLaunchResult(
                    success = false,
                    errorMessage = "Java ${instance.javaVersion.version} not found"
                )
            }

            val gameDir = File(instance.gameDirectory)
            gameDir.mkdirs()

            val modsDir = File(instance.modDirectory)
            modsDir.mkdirs()

            val processBuilder = ProcessBuilder().apply {
                command(
                    javaPath,
                    "-Xmx${instance.ram}M",
                    "-Xms${instance.ram / 2}M",
                    *instance.jvmArgs.split(" ").toTypedArray(),
                    "-Dminecraft.launcher.brand=modrinth",
                    "-Dminecraft.launcher.version=${getVersionString()}",
                    "-cp", getClassPath(instance),
                    "net.minecraft.client.main.Main",
                    "--gameDir", gameDir.absolutePath,
                    "--assetsDir", "${gameDir.absolutePath}/assets",
                    "--assetIndex", instance.gameVersion,
                    "--username", account.username,
                    "--uuid", account.uuid,
                    "--accessToken", account.accessToken,
                    "--version", instance.gameVersion,
                    "--width", "1280",
                    "--height", "720"
                )
                directory(gameDir)
                redirectErrorStream(true)
            }

            gameProcess = processBuilder.start()
            processId = getProcessId(gameProcess!!) ?: 0

            monitorGameOutput()

            GameLaunchResult(
                success = true,
                processId = processId
            )
        } catch (e: Exception) {
            Log.e("GameLauncher", "Failed to launch game", e)
            GameLaunchResult(
                success = false,
                errorMessage = "Launch failed: ${e.message}"
            )
        }
    }

    override suspend fun stop(): Boolean = withContext(Dispatchers.Default) {
        return@withContext try {
            gameProcess?.destroy()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun isRunning(): Boolean {
        return gameProcess?.isAlive ?: false
    }

    override fun getGameProcess(): Process? {
        return gameProcess
    }

    private fun getJavaPath(javaVersion: Int): String {
        val javaHome = System.getenv("JAVA_HOME") ?: "/system/framework"
        return "$javaHome/bin/java"
    }

    private fun getClassPath(instance: GameInstance): String {
        val modsDir = File(instance.modDirectory)
        val classPaths = mutableListOf<String>()
        modsDir.listFiles()?.forEach { file ->
            if (file.extension in listOf("jar", "zip")) {
                classPaths.add(file.absolutePath)
            }
        }
        return classPaths.joinToString(":")
    }

    private fun getVersionString(): String = "1.0.0"

    private fun getProcessId(process: Process): Int? {
        return try {
            val processField = process.javaClass.getDeclaredField("pid")
            processField.isAccessible = true
            processField.getInt(process)
        } catch (e: Exception) {
            null
        }
    }

    private fun monitorGameOutput() {
        Thread {
            try {
                val reader = BufferedReader(InputStreamReader(gameProcess!!.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    Log.d("GameOutput", line!!)
                    // Parse for crashes
                    if (line!!.contains("Exception", ignoreCase = true) ||
                        line!!.contains("Error", ignoreCase = true) ||
                        line!!.contains("CRITICAL", ignoreCase = true)
                    ) {
                        // Log crash for analysis
                    }
                }
            } catch (e: Exception) {
                Log.e("GameMonitor", "Error monitoring output", e)
            }
        }.start()
    }
}
