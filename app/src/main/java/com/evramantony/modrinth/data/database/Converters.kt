package com.evramantony.modrinth.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.evramantony.modrinth.data.model.ControlButton
import com.evramantony.modrinth.data.model.JoystickPosition

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromButtonListJson(value: String?): List<ControlButton>? {
        return if (value == null) null
        else gson.fromJson(value, Array<ControlButton>::class.java).toList()
    }

    @TypeConverter
    fun toButtonListJson(value: List<ControlButton>?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun fromJoystickPositionJson(value: String?): JoystickPosition? {
        return if (value == null) null
        else gson.fromJson(value, JoystickPosition::class.java)
    }

    @TypeConverter
    fun toJoystickPositionJson(value: JoystickPosition?): String? {
        return if (value == null) null else gson.toJson(value)
    }
}
