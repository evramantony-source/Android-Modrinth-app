package com.evramantony.modrinthapp.data.api

import retrofit2.http.GET

interface MinecraftApiService {
    @GET("mc/game/version_manifest/2.json")
    suspend fun getVersionManifest(): VersionManifestResponse
}

data class VersionManifestResponse(
    val latest: LatestVersionResponse,
    val versions: List<VersionResponse>
)

data class LatestVersionResponse(
    val release: String,
    val snapshot: String
)

data class VersionResponse(
    val name: String,
    val type: String,
    val releaseTime: String,
    val url: String,
    val sha1: String,
    val complianceLevel: String
)
