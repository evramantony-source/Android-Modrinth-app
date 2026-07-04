package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "installed_content")
data class InstalledContent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val instanceId: Int,
    val contentId: String,
    val name: String,
    val type: ContentType,
    val source: ContentSource,
    val version: String,
    val filePath: String,
    val fileSize: Long,
    val sha1: String,
    val enabled: Boolean = true,
    val installedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class ContentType {
    MOD,
    MODPACK,
    RESOURCE_PACK,
    SHADER_PACK,
    DATA_PACK
}

enum class ContentSource {
    MODRINTH,
    CURSEFORGE,
    LOCAL
}
