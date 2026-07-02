package com.evramantony.modrinth.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "java_runtimes")
data class JavaRuntimeEntity(
    @PrimaryKey
    val version: String,
    val majorVersion: Int,
    val downloadUrl: String,
    val size: Long,
    val sha256: String,
    val isInstalled: Boolean = false,
    val installPath: String? = null,
    val installDate: Long? = null,
    val lastUsed: Long? = null
)

@Entity(tableName = "renderer_preferences")
data class RendererPreferenceEntity(
    @PrimaryKey
    val versionId: String,
    val preferredRenderer: String,
    val useNativeTextures: Boolean = true,
    val maxFPS: Int = 60,
    val textureQuality: String = "medium",
    val renderDistance: Int = 8
)

@Entity(tableName = "device_graphics")
data class DeviceGraphicsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val manufacturer: String,
    val model: String,
    val androidVersion: Int,
    val primaryRenderer: String,
    val supportedRenderers: String, // JSON array stored as string
    val glVersion: String?,
    val vendor: String?,
    val maxTextureSize: Int,
    val maxRenderSize: Int,
    val detectionDate: Long
)
