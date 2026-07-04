package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class MinecraftVersion(
    val id: String,
    val type: VersionType,
    val releaseTime: Date,
    val url: String,
    val sha1: String,
    val complianceLevel: String,
    val javaVersion: Int = 8
) : Parcelable

enum class VersionType {
    RELEASE,
    SNAPSHOT,
    OLD_ALPHA,
    OLD_BETA,
    PRE_RELEASE
}

@Parcelize
data class VersionManifest(
    val latest: LatestVersions,
    val versions: List<MinecraftVersion>
) : Parcelable

@Parcelize
data class LatestVersions(
    val release: String,
    val snapshot: String
) : Parcelable
