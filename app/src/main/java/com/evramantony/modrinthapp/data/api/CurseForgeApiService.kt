package com.evramantony.modrinthapp.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurseForgeApiService {
    @GET("v1/mods/search")
    suspend fun searchMods(
        @Query("searchFilter") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("index") page: Int = 0,
        @Query("classId") classId: Int = 6  // 6 = Mods
    ): CurseForgSearchResponse

    @GET("v1/mods")
    suspend fun getMods(
        @Query("modIds") modIds: String
    ): CurseForgeModsResponse

    @GET("v1/mods/{id}")
    suspend fun getModDetails(
        @Path("id") modId: Int
    ): CurseForgeModResponse

    @GET("v1/mods/{id}/files")
    suspend fun getModFiles(
        @Path("id") modId: Int,
        @Query("pageSize") pageSize: Int = 50,
        @Query("index") page: Int = 0
    ): CurseForgeFilesResponse

    @GET("v1/classes")
    suspend fun getClasses(): CurseForgeClassesResponse
}

data class CurseForgSearchResponse(
    val data: List<CurseForgeModData>,
    val pagination: CurseForgePagination
)

data class CurseForgeModsResponse(
    val data: List<CurseForgeModData>
)

data class CurseForgeModResponse(
    val data: CurseForgeModData
)

data class CurseForgeFilesResponse(
    val data: List<CurseForgeFileData>,
    val pagination: CurseForgePagination
)

data class CurseForgeClassesResponse(
    val data: List<CurseForgeClass>
)

data class CurseForgeModData(
    val id: Int,
    val name: String,
    val summary: String,
    val authors: List<CurseForgeAuthor>,
    val downloadCount: Long,
    val logo: CurseForgeLogo?,
    val links: CurseForgeLinks,
    val categoriesIds: List<Int>
)

data class CurseForgeFileData(
    val id: Int,
    val fileName: String,
    val displayName: String,
    val fileLength: Long,
    val downloadUrl: String,
    val hashes: List<CurseForgeHash>,
    val releaseType: String,
    val gameVersions: List<String>,
    val dependencies: List<CurseForgeDependency>
)

data class CurseForgeAuthor(
    val id: Int,
    val name: String
)

data class CurseForgeLogo(
    val url: String,
    val thumbnailUrl: String
)

data class CurseForgeLinks(
    val websiteUrl: String?,
    val wikiUrl: String?,
    val issuesUrl: String?,
    val sourceUrl: String?
)

data class CurseForgeHash(
    val value: String,
    val algo: String
)

data class CurseForgeDependency(
    val modId: Int,
    val relationType: String  // "requiredDependency", "optionalDependency", etc
)

data class CurseForgeClass(
    val classId: Int,
    val classesName: String
)

data class CurseForgePagination(
    val index: Int,
    val pageSize: Int,
    val resultCount: Int,
    val totalCount: Int
)
