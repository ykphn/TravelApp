package com.example.travelapp.data.location.repository

import android.app.Application
import android.location.Location
import com.example.travelapp.data.location.api.LocationManager
import com.example.travelapp.utility.ScreenState
import com.example.travelapp.utility.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LocationManagerRepository(
    private val context: Application,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationManager {

    override suspend fun getCurrentLocation(): Flow<ScreenState<Location?>> {
        return flow {
            if (context.hasLocationPermission()) {
                try {
                    val locationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        CancellationTokenSource().token
                    )
                    emit(ScreenState.Loading())
                    val location = locationTask.await()
                    emit(ScreenState.Success(location))
                } catch (e: SecurityException) {
                    emit(ScreenState.Error(message = "", boolean = false))
                }
            } else {
                emit(ScreenState.Error(message = "", boolean = false))
            }
        }

    }

    override suspend fun getLastLocation(): Flow<ScreenState<Location?>> {
        return flow {
            if (context.hasLocationPermission()) {
                try {
                    val locationTask: Task<Location> = fusedLocationClient.lastLocation
                    emit(ScreenState.Loading())
                    val location = locationTask.await()
                    emit(ScreenState.Success(location))
                } catch (e: SecurityException) {
                    emit(ScreenState.Error(message = "", boolean = false))
                }
            } else {
                emit(ScreenState.Error(message = "", boolean = false))
            }
        }
    }

}
