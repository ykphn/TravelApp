package com.example.travelapp.data.remote.repository

import com.example.travelapp.data.model.OverpassResponse
import kotlinx.coroutines.flow.Flow

interface OverpassRepository {
    suspend fun getPlaces(query: String): Flow<OverpassResponse>
}