package com.oseo27jul.rickandmorty.data

import com.oseo27jul.rickandmorty.data.model.EpisodeProvider
import com.oseo27jul.rickandmorty.data.model.Episodes
import com.oseo27jul.rickandmorty.data.model.EpisodesResponse
import com.oseo27jul.rickandmorty.data.network.EpisodeService

class EpisodesRepository {
    private val api = EpisodeService()

    suspend fun getEpisode(page: Int): EpisodesResponse {
        val response = api.getEpisodes(page)
        EpisodeProvider.episodeProvider = response
        return response
    }

    suspend fun getListEpisodes(page: Int): List<Episodes> {
        return api.getEpisodes(page).results
    }
}