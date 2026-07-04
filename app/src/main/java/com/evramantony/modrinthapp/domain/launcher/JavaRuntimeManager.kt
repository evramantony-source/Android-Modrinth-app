package com.evramantony.modrinthapp.domain.launcher

import com.evramantony.modrinthapp.data.models.JavaVersion
import java.io.File

interface JavaRuntimeManager {
    suspend fun getJavaPath(javaVersion: JavaVersion): String
    suspend fun ensureJavaInstalled(javaVersion: JavaVersion): Boolean
    suspend fun downloadJava(javaVersion: JavaVersion): Boolean
    suspend fun validateJava(javaVersion: JavaVersion): Boolean
    fun listInstalledJavaVersions(): List<JavaVersion>
    fun getJavaHomeDirectory(): File
}
