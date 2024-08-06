package com.example.travelapp.data.remote.repository

import com.example.travelapp.data.model.OverpassResponse
import com.example.travelapp.data.remote.api.OverpassApi
import javax.inject.Inject

class OverpassRepositoryImp @Inject constructor(
    private val overpassApi: OverpassApi
) : OverpassRepository {
    override suspend fun getPlaces(query: String): OverpassResponse {
        return overpassApi.getPlaces(query)
    }
}