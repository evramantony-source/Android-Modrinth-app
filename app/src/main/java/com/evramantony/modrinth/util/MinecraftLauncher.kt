package com.evramantony.modrinth.util

import android.content.Context
import android.os.Build
import com.evramantony.modrinth.data.model.GraphicsRenderer
import com.evramantony.modrinth.data.model.JavaVersion
import java.io.File

class MinecraftLauncher(private val context: Context) {
    
    fun getLaunchCommand(
        javaPath: String,
        gameDirectory: String,
        version: String,
        username: String,
        uuid: String?,
        accessToken: String?,
        javaVersion: JavaVersion,
        renderer: GraphicsRenderer,
        modLoader: String? = null,
        maxMemoryMB: Int = 2048
    ): List<String> {
        val command = mutableListOf<String>()
        command.add(javaPath)
        
        // Get optimized JVM args for the selected renderer
        val rendererOptimizer = RendererOptimizer()
        val jvmArgs = rendererOptimizer.getJVMArgsForRenderer(renderer, maxMemoryMB)
        command.addAll(jvmArgs)
        
        // Native libraries path
        command.add("-Djava.library.path=$gameDirectory/natives")
        
        // Main class
        command.add("-cp")
        command.add(getClasspathForVersion(gameDirectory, version, modLoader))
        command.add("net.minecraft.client.main.Main")
        
        // Game Arguments
        command.add("--username")
        command.add(username)
        
        if (uuid != null && accessToken != null) {
            command.add("--uuid")
            command.add(uuid)
            command.add("--accessToken")
            command.add(accessToken)
        } else {
            command.add("--uuid")
            command.add("0")
            command.add("--accessToken")
            command.add("0")
        }
        
        command.add("--gameDir")
        command.add(gameDirectory)
        
        command.add("--assetsDir")
        command.add("$gameDirectory/assets")
        
        command.add("--assetIndex")
        command.add(version)
        
        command.add("--versionType")
        command.add("release")
        
        // Add renderer-specific game args
        command.addAll(rendererOptimizer.getGameArgsForRenderer(renderer))
        
        return command
    }
    
    private fun getClasspathForVersion(
        gameDirectory: String,
        version: String,
        modLoader: String?
    ): String {
        val separator = ":"
        val paths = mutableListOf<String>()
        
        // Add libraries
        val librariesDir = File(gameDirectory, "libraries")
        if (librariesDir.exists()) {
            librariesDir.walkTopDown().forEach { file ->
                if (file.isFile && file.name.endsWith(".jar")) {
                    paths.add(file.absolutePath)
                }
            }
        }
        
        // Add mods if mod loader is specified
        if (modLoader != null) {
            val modsDir = File(gameDirectory, "mods")
            if (modsDir.exists()) {
                modsDir.walkTopDown().forEach { file ->
                    if (file.isFile && file.name.endsWith(".jar")) {
                        paths.add(file.absolutePath)
                    }
                }
            }
        }
        
        // Add client JAR
        val clientJar = File(gameDirectory, "versions/$version/$version.jar")
        if (clientJar.exists()) {
            paths.add(clientJar.absolutePath)
        }
        
        return paths.joinToString(separator)
    }
    
    fun getJavaPath(javaVersion: JavaVersion): String? {
        val manager = JavaDownloadManager(context)
        val javaBinary = manager.getJavaBinaryPath(javaVersion)
        return if (javaBinary.exists()) javaBinary.absolutePath else null
    }
}
