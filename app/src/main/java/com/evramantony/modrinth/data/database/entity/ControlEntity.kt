package com.evramantony.modrinth.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.evramantony.modrinth.data.database.Converters

@Entity(tableName = "control_layouts")
data class ControlLayoutEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val buttonsJson: String,  // JSON serialized
    val joystickLeftJson: String,
    val joystickRightJson: String,
    val opacity: Float = 0.8f,
    val scale: Float = 1.0f,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)

@Entity(tableName = "key_bindings")
data class KeyBindingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val layoutId: String,
    val action: String,
    val keyCode: Int,
    val isMod: Boolean = false
)
