package com.example.soundconnect.data.remote

import com.example.soundconnect.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApi {
    @GET("search")
    suspend fun searchTracks(@Query("q") query: String): SearchResponse

    companion object {
        const val BASE_URL = "https://api.deezer.com/"
    }
}
