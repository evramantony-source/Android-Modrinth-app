package com.evramantony.modrinthapp.di

import android.content.Context
import com.evramantony.modrinthapp.data.api.CurseForgeApiService
import com.evramantony.modrinthapp.data.api.MinecraftApiService
import com.evramantony.modrinthapp.data.api.ModrinthApiService
import com.evramantony.modrinthapp.data.database.LauncherDatabase
import com.evramantony.modrinthapp.data.repository.AccountRepositoryImpl
import com.evramantony.modrinthapp.data.repository.ContentRepositoryImpl
import com.evramantony.modrinthapp.data.repository.CrashLogRepositoryImpl
import com.evramantony.modrinthapp.data.repository.InstanceRepositoryImpl
import com.evramantony.modrinthapp.domain.repository.AccountRepository
import com.evramantony.modrinthapp.domain.repository.ContentRepository
import com.evramantony.modrinthapp.domain.repository.CrashLogRepository
import com.evramantony.modrinthapp.domain.repository.InstanceRepository
import com.evramantony.modrinthapp.presentation.launcher.GameLauncherImpl
import com.evramantony.modrinthapp.presentation.launcher.GameOutputMonitorImpl
import com.evramantony.modrinthapp.presentation.launcher.SettingsManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LauncherDatabase {
        return LauncherDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideModrinthApiService(): ModrinthApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.modrinth.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ModrinthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCurseForgeApiService(): CurseForgeApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.curseforge.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurseForgeApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMinecraftApiService(): MinecraftApiService {
        return Retrofit.Builder()
            .baseUrl("https://launcher.mojang.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideInstanceRepository(database: LauncherDatabase): InstanceRepository {
        return InstanceRepositoryImpl(database.instanceDao())
    }

    @Singleton
    @Provides
    fun provideAccountRepository(database: LauncherDatabase): AccountRepository {
        return AccountRepositoryImpl(database.accountDao())
    }

    @Singleton
    @Provides
    fun provideContentRepository(database: LauncherDatabase): ContentRepository {
        return ContentRepositoryImpl(database.contentDao(), database.dependencyDao())
    }

    @Singleton
    @Provides
    fun provideCrashLogRepository(database: LauncherDatabase): CrashLogRepository {
        return CrashLogRepositoryImpl(database.crashLogDao())
    }

    @Singleton
    @Provides
    fun provideGameLauncher(@ApplicationContext context: Context) = GameLauncherImpl(context)

    @Singleton
    @Provides
    fun provideSettingsManager(@ApplicationContext context: Context) = SettingsManagerImpl(context)

    @Singleton
    @Provides
    fun provideGameOutputMonitor(@ApplicationContext context: Context) = GameOutputMonitorImpl(context)
}
