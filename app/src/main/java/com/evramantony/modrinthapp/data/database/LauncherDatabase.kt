package com.evramantony.modrinthapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evramantony.modrinthapp.data.models.GameAccount
import com.evramantony.modrinthapp.data.models.GameInstance
import com.evramantony.modrinthapp.data.models.InstalledContent
import com.evramantony.modrinthapp.data.models.ModDependency
import com.evramantony.modrinthapp.data.models.CrashLog

@Database(
    entities = [
        GameInstance::class,
        GameAccount::class,
        InstalledContent::class,
        ModDependency::class,
        CrashLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun instanceDao(): InstanceDao
    abstract fun accountDao(): AccountDao
    abstract fun contentDao(): ContentDao
    abstract fun dependencyDao(): DependencyDao
    abstract fun crashLogDao(): CrashLogDao

    companion object {
        @Volatile
        private var INSTANCE: LauncherDatabase? = null

        fun getDatabase(context: Context): LauncherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LauncherDatabase::class.java,
                    "launcher_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
