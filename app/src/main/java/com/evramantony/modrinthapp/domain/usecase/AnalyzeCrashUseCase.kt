package com.evramantony.modrinthapp.domain.usecase

import com.evramantony.modrinthapp.domain.repository.CrashLogRepository

class AnalyzeCrashUseCase(
    private val crashLogRepository: CrashLogRepository
) {
    suspend fun execute(crashId: Int) = crashLogRepository.analyzeCrash(crashId)
}
