package com.example.travelapp.data.remote.api

import com.example.travelapp.data.model.OverpassResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OverpassApi {
    @GET("interpreter?data=")
    suspend fun getPlaces(@Query("data") query: String): OverpassResponse
}
