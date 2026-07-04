package com.evramantony.modrinthapp.data.repository

import com.evramantony.modrinthapp.data.database.InstanceDao
import com.evramantony.modrinthapp.data.models.GameInstance
import com.evramantony.modrinthapp.domain.repository.InstanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstanceRepositoryImpl(
    private val instanceDao: InstanceDao
) : InstanceRepository {

    override suspend fun getAllInstances(): List<GameInstance> {
        return withContext(Dispatchers.IO) {
            instanceDao.getAllInstances()
        }
    }

    override suspend fun getInstanceById(id: Int): GameInstance? {
        return withContext(Dispatchers.IO) {
            instanceDao.getInstanceById(id)
        }
    }

    override suspend fun createInstance(instance: GameInstance): Long {
        return withContext(Dispatchers.IO) {
            instanceDao.insertInstance(instance)
        }
    }

    override suspend fun updateInstance(instance: GameInstance) {
        return withContext(Dispatchers.IO) {
            instanceDao.updateInstance(instance)
        }
    }

    override suspend fun deleteInstance(id: Int) {
        return withContext(Dispatchers.IO) {
            val instance = instanceDao.getInstanceById(id) ?: return@withContext
            instanceDao.deleteInstance(instance)
        }
    }

    override suspend fun launchInstance(id: Int): Boolean {
        // This will be implemented by GameLauncher
        return false
    }
}
