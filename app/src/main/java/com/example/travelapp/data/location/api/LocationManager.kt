package com.example.travelapp.data.location.api

import android.location.Location
import com.example.travelapp.utility.ScreenState
import kotlinx.coroutines.flow.Flow

interface LocationManager {
    suspend fun getCurrentLocation(): Flow<ScreenState<Location?>>
    suspend fun getLastLocation(): Flow<ScreenState<Location?>>
}