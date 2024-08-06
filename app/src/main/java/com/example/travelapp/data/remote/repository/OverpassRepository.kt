package com.example.travelapp.data.remote.repository

import com.example.travelapp.data.model.OverpassResponse

interface OverpassRepository {
    suspend fun getPlaces(query: String): OverpassResponse
}