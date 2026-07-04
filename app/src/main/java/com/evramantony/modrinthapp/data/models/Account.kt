package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "accounts")
data class GameAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val isOffline: Boolean = true,
    val uuid: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val clientToken: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = 0,
    val isActive: Boolean = false
) : Parcelable
