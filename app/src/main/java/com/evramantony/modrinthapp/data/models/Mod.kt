package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mods")
data class Mod(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val downloads: Int,
    val followers: Int,
    val iconUrl: String,
    val projectUrl: String,
    val gameVersions: List<String>,
    val loaders: List<String>,
    val isInstalled: Boolean = false,
    val localPath: String = ""
) : Parcelable

@Parcelize
data class ModFile(
    val id: String,
    val modId: String,
    val filename: String,
    val size: Long,
    val downloadUrl: String,
    val sha1: String,
    val gameVersion: String,
    val loader: String
) : Parcelable
