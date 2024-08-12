package com.example.travelapp.data.remote.repository

import com.example.travelapp.data.model.OverpassResponse
import com.example.travelapp.data.remote.api.OverpassApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OverpassRepositoryImp @Inject constructor(
    private val overpassApi: OverpassApi
) : OverpassRepository {
    override suspend fun getPlaces(query: String): Flow<OverpassResponse> {
        return flow {
            try {
                val response = overpassApi.getPlaces(query)
                emit(response)
            } catch (e: Exception) {
                println(e.message ?: "error")
            }
        }
    }

}