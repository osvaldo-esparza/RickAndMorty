package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.core.RetrofitHelper
import com.oseo27jul.rickandmorty.data.model.EpisodesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpisodeService {
    private val retrofit = RetrofitHelper.getRetrofitInstance()

    suspend fun getEpisodes(page:Int): EpisodesResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(EpisodesApiClient::class.java).getEpisodes(page)
            (response.body() ?: EpisodesResponse())
        }
    }
}