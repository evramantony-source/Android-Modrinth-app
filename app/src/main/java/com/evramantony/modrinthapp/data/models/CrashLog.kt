package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "crash_logs")
data class CrashLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val instanceId: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val gameVersion: String,
    val crashMessage: String,
    val errorType: String,
    val stackTrace: String,
    val relevantMods: String = "",
    val javaVersion: String,
    val nativeCrash: Boolean = false
) : Parcelable

data class CrashAnalysis(
    val isCrashAnalyzed: Boolean,
    val primaryReason: String,
    val affectedMods: List<String>,
    val suggestions: List<String>,
    val similarCrashes: List<CrashLog>
)
