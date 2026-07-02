package com.evramantony.modrinth.di

import android.content.Context
import com.evramantony.modrinth.data.api.MinecraftApi
import com.evramantony.modrinth.data.api.ModrinthApi
import com.evramantony.modrinth.data.database.AppDatabase
import com.evramantony.modrinth.data.repository.JavaRuntimeRepository
import com.evramantony.modrinth.data.repository.MinecraftRepository
import com.evramantony.modrinth.data.repository.ModRepository
import com.evramantony.modrinth.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideModrinthApi(): ModrinthApi {
        return Retrofit.Builder()
            .baseUrl("https://api.modrinth.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ModrinthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMinecraftApi(): MinecraftApi {
        return Retrofit.Builder()
            .baseUrl("https://launcher.mojang.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MinecraftApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideUserRepository(database: AppDatabase): UserRepository {
        return UserRepository(database.userDao())
    }

    @Singleton
    @Provides
    fun provideModRepository(api: ModrinthApi, database: AppDatabase): ModRepository {
        return ModRepository(api, database.modDao())
    }

    @Singleton
    @Provides
    fun provideMinecraftRepository(
        api: MinecraftApi,
        database: AppDatabase,
        @ApplicationContext context: Context
    ): MinecraftRepository {
        return MinecraftRepository(api, database.minecraftDao(), context)
    }

    @Singleton
    @Provides
    fun provideJavaRuntimeRepository(
        database: AppDatabase,
        @ApplicationContext context: Context
    ): JavaRuntimeRepository {
        return JavaRuntimeRepository(
            database.javaRuntimeDao(),
            database.rendererPreferenceDao(),
            database.deviceGraphicsDao(),
            context
        )
    }
}
