package com.example.travelapp.data.remote.query

import javax.inject.Inject

class OverpassQueryProviderFactory @Inject constructor(
    private val overpassBuildQuery: OverpassBuildQuery
) {
    fun create(latitude: Double, longitude: Double, distance: Int): OverpassQueryProvider {
        return OverpassQueryProvider(latitude, longitude, distance, overpassBuildQuery)
    }
}
