package com.evramantony.modrinthapp.domain.repository

import com.evramantony.modrinthapp.data.models.GameInstance

interface InstanceRepository {
    suspend fun getAllInstances(): List<GameInstance>
    suspend fun getInstanceById(id: Int): GameInstance?
    suspend fun createInstance(instance: GameInstance): Long
    suspend fun updateInstance(instance: GameInstance)
    suspend fun deleteInstance(id: Int)
    suspend fun launchInstance(id: Int): Boolean
}
