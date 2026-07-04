package com.evramantony.modrinthapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ModrinthApiService {
    @GET("v2/search")
    suspend fun searchMods(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): SearchResponse

    @GET("v2/project/{id}")
    suspend fun getModDetails(
        @Path("id") modId: String
    ): ModDetailsResponse

    @GET("v2/project/{id}/version")
    suspend fun getModVersions(
        @Path("id") modId: String,
        @Query("game_versions") gameVersions: String = "",
        @Query("loaders") loaders: String = ""
    ): List<ModVersionResponse>
}

data class SearchResponse(
    val hits: List<ModDetailsResponse>,
    val offset: Int,
    val limit: Int,
    val total_hits: Int
)

data class ModDetailsResponse(
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val downloads: Int,
    val followers: Int,
    val icon_url: String,
    val project_url: String,
    val game_versions: List<String>,
    val loaders: List<String>
)

data class ModVersionResponse(
    val id: String,
    val version_number: String,
    val name: String,
    val files: List<ModFileResponse>,
    val game_versions: List<String>,
    val loaders: List<String>
)

data class ModFileResponse(
    val url: String,
    val filename: String,
    val size: Long,
    val hashes: Map<String, String>
)
