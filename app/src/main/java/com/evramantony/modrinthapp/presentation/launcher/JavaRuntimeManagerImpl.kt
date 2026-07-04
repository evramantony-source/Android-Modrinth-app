package com.evramantony.modrinthapp.presentation.launcher

import android.content.Context
import android.os.Build
import com.evramantony.modrinthapp.domain.launcher.JavaRuntimeManager
import com.evramantony.modrinthapp.data.models.JavaVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JavaRuntimeManagerImpl(
    private val context: Context
) : JavaRuntimeManager {

    private val javaDir = File(context.filesDir, "java_runtime")

    init {
        javaDir.mkdirs()
    }

    override suspend fun getJavaPath(javaVersion: JavaVersion): String = withContext(Dispatchers.IO) {
        val javaHome = File(javaDir, javaVersion.path)
        File(javaHome, "bin/java").absolutePath
    }

    override suspend fun ensureJavaInstalled(javaVersion: JavaVersion): Boolean = withContext(Dispatchers.IO) {
        val javaHome = File(javaDir, javaVersion.path)
        if (javaHome.exists() && File(javaHome, "bin/java").exists()) {
            return@withContext true
        }
        downloadJava(javaVersion)
    }

    override suspend fun downloadJava(javaVersion: JavaVersion): Boolean = withContext(Dispatchers.IO) {
        try {
            // In a real implementation, download from adoptopenjdk or similar
            // For now, we'll create placeholder directories
            val javaHome = File(javaDir, javaVersion.path)
            javaHome.mkdirs()
            File(javaHome, "bin").mkdirs()
            File(javaHome, "lib").mkdirs()
            File(javaHome, "lib/modules").mkdirs()

            // Create placeholder java executable
            File(javaHome, "bin/java").apply {
                parentFile?.mkdirs()
                createNewFile()
                setExecutable(true)
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun validateJava(javaVersion: JavaVersion): Boolean = withContext(Dispatchers.IO) {
        try {
            val javaPath = getJavaPath(javaVersion)
            val process = Runtime.getRuntime().exec(arrayOf(javaPath, "-version"))
            process.waitFor()
            process.exitValue() == 0
        } catch (e: Exception) {
            false
        }
    }

    override fun listInstalledJavaVersions(): List<JavaVersion> {
        val installed = mutableListOf<JavaVersion>()
        JavaVersion.values().forEach { version ->
            val javaHome = File(javaDir, version.path)
            if (File(javaHome, "bin/java").exists()) {
                installed.add(version)
            }
        }
        return installed
    }

    override fun getJavaHomeDirectory(): File {
        return javaDir
    }
}
