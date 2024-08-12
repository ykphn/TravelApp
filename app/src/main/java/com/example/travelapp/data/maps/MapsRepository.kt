package com.example.travelapp.data.maps

import com.example.travelapp.utility.ScreenState

import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.flow.Flow


interface MapsRepository {

    suspend fun searchPlacesByText(query: String): Flow<ScreenState<List<Place>>>
}