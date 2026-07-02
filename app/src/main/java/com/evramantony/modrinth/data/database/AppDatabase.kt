package com.evramantony.modrinth.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evramantony.modrinth.data.database.entity.InstalledModEntity
import com.evramantony.modrinth.data.database.entity.UserEntity
import com.evramantony.modrinth.data.database.entity.MinecraftInstallation
import com.evramantony.modrinth.data.database.entity.JavaRuntimeEntity
import com.evramantony.modrinth.data.database.entity.RendererPreferenceEntity
import com.evramantony.modrinth.data.database.entity.DeviceGraphicsEntity

@Database(
    entities = [
        UserEntity::class,
        InstalledModEntity::class,
        MinecraftInstallation::class,
        JavaRuntimeEntity::class,
        RendererPreferenceEntity::class,
        DeviceGraphicsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun modDao(): ModDao
    abstract fun minecraftDao(): MinecraftDao
    abstract fun javaRuntimeDao(): JavaRuntimeDao
    abstract fun rendererPreferenceDao(): RendererPreferenceDao
    abstract fun deviceGraphicsDao(): DeviceGraphicsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "modrinth_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
