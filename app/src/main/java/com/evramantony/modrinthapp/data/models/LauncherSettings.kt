package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "launcher_settings")
data class LauncherSettings(
    @PrimaryKey
    val id: Int = 0,
    val defaultRam: Int = 2048,
    val maxRam: Int = 4096,
    val jvmArgs: String = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled",
    val gameWidth: Int = 1280,
    val gameHeight: Int = 720,
    val renderer: String = "OPENGL_ES",
    val autoUpdate: Boolean = true,
    val showCrashReports: Boolean = true,
    val enableTouchControls: Boolean = true,
    val touchControlsOpacity: Float = 0.7f,
    val touchControlsSize: Float = 1.0f,
    val keepLauncher: Boolean = false,
    val fastStart: Boolean = false
) : Parcelable
