package com.evramantony.modrinthapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mod_dependencies")
data class ModDependency(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val modId: String,
    val dependencyModId: String,
    val dependencyType: DependencyType,
    val versionRange: String = "*"
) : Parcelable

enum class DependencyType {
    REQUIRED,
    OPTIONAL,
    INCOMPATIBLE,
    EMBEDDED
}
