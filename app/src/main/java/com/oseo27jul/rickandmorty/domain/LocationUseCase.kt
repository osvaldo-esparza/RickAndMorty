package com.oseo27jul.rickandmorty.domain

import com.oseo27jul.rickandmorty.data.LocationRepository
import com.oseo27jul.rickandmorty.data.model.LocationResponse
import com.oseo27jul.rickandmorty.data.model.Locations

class LocationUseCase {

    val repository = LocationRepository()

    suspend operator  fun invoke(page:Int): LocationResponse = repository.getLocation(page)

    suspend fun getListLocation(page: Int):List<Locations> = repository.getListLocation(page)
}