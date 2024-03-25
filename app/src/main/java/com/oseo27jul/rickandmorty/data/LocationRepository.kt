package com.oseo27jul.rickandmorty.data

import com.oseo27jul.rickandmorty.data.model.LocationProvider
import com.oseo27jul.rickandmorty.data.model.LocationResponse
import com.oseo27jul.rickandmorty.data.model.Locations
import com.oseo27jul.rickandmorty.data.network.LocationService

class LocationRepository {
    private val api = LocationService()

    suspend fun getLocation(page : Int):LocationResponse{
        val response = api.getLocations(page)
        LocationProvider.locationRepository = response
        return response
    }

    suspend fun getListLocation(page: Int): List<Locations>{
        val response = api.getLocations(page).results
        return response

    }

}