package com.evramantony.modrinthapp.domain.usecase

import com.evramantony.modrinthapp.data.models.ContentSource
import com.evramantony.modrinthapp.data.models.ContentType
import com.evramantony.modrinthapp.domain.repository.ContentRepository

class DependencyResolutionUseCase(
    private val contentRepository: ContentRepository
) {
    suspend fun resolveAndInstallDependencies(
        modId: String,
        instanceId: Int,
        source: ContentSource
    ): List<String> {
        val dependencies = contentRepository.getDependencies(modId)
        val installedMods = mutableListOf<String>()

        dependencies.forEach { dependency ->
            val depType = dependency.dependencyType.toString()
            if (depType == "REQUIRED" || depType == "OPTIONAL") {
                try {
                    // Recursively resolve dependencies
                    val subDeps = resolveAndInstallDependencies(
                        dependency.dependencyModId,
                        instanceId,
                        source
                    )
                    installedMods.addAll(subDeps)
                    installedMods.add(dependency.dependencyModId)
                } catch (e: Exception) {
                    // Log error
                }
            }
        }

        return installedMods
    }
}
