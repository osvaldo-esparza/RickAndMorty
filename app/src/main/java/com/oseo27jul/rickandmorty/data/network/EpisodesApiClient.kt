package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.data.model.EpisodesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodesApiClient {
    @GET("episode")
    suspend fun getEpisodes(@Query("page")page:Int) : Response<EpisodesResponse>
}