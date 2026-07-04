package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "game_instances")
data class GameInstance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gameVersion: String,
    val javaVersion: JavaVersion = JavaVersion.JAVA_8,
    val modDirectory: String,
    val shaderDirectory: String = "",
    val resourcePackDirectory: String = "",
    val gameDirectory: String,
    val ram: Int = 2048,
    val jvmArgs: String = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled",
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long = 0
) : Parcelable

enum class JavaVersion(val version: Int, val path: String) {
    JAVA_8(8, "java8"),
    JAVA_17(17, "java17"),
    JAVA_21(21, "java21"),
    JAVA_25(25, "java25")
}
