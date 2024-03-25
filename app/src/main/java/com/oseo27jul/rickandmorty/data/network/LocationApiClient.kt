package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.data.model.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiClient {

    @GET("location")
    suspend fun getLocation(@Query("page") page:Int): Response<LocationResponse>
}