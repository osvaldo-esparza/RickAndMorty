package com.oseo27jul.rickandmorty.domain

import com.oseo27jul.rickandmorty.data.EpisodesRepository
import com.oseo27jul.rickandmorty.data.model.Episodes
import com.oseo27jul.rickandmorty.data.model.EpisodesResponse

class EpisodesUseCase {

    private val repository = EpisodesRepository()

    suspend operator fun invoke(page: Int): EpisodesResponse = repository.getEpisode(page)

    suspend fun getEpisode(page: Int):List<Episodes> = repository.getListEpisodes(page)
}