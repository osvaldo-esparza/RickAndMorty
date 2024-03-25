package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.data.model.CharactersModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApiClient {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharactersModel>
}