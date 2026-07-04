package com.evramantony.modrinthapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ModrinthApiService {
    @GET("v2/search")
    suspend fun searchContent(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("facets") facets: String
    ): SearchResponse

    @GET("v2/project/{id}")
    suspend fun getContentDetails(
        @Path("id") contentId: String
    ): ContentDetailsResponse

    @GET("v2/project/{id}/version")
    suspend fun getContentVersions(
        @Path("id") contentId: String,
        @Query("game_versions") gameVersions: String = "",
        @Query("loaders") loaders: String = ""
    ): List<VersionResponse>
}

data class SearchResponse(
    val hits: List<ContentDetailsResponse>,
    val offset: Int,
    val limit: Int,
    val total_hits: Int
)

data class ContentDetailsResponse(
    val id: String,
    val name: String,
    val description: String,
    val author: String,
    val downloads: Int,
    val followers: Int,
    val icon_url: String,
    val project_url: String,
    val game_versions: List<String>,
    val loaders: List<String>,
    val project_type: String,
    val dependencies: List<DependencyResponse>?
)

data class VersionResponse(
    val id: String,
    val version_number: String,
    val name: String,
    val files: List<FileResponse>,
    val game_versions: List<String>,
    val loaders: List<String>,
    val dependencies: List<DependencyResponse>
)

data class FileResponse(
    val url: String,
    val filename: String,
    val size: Long,
    val hashes: Map<String, String>
)

data class DependencyResponse(
    val project_id: String,
    val version_id: String?,
    val dependency_type: String
)
