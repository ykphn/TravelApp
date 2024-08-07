package com.example.travelapp.data.remote.query

import javax.inject.Inject

class OverpassQueryProvider @Inject constructor(
    private val latitude: Double,
    private val longitude: Double,
    private val distance: Int,
    private val overpassBuildQuery: OverpassBuildQuery
) : OverpassQuery {

    private fun buildQuery(vararg filters: Pair<String, String>): String {
        return overpassBuildQuery.buildQuery(latitude, longitude, distance, *filters)
    }

    override fun getQueryMuseumAndArchaeological(): String {
        return buildQuery(
            "tourism" to "museum",
            "historic" to "archaeological_site"
        )
    }

    override fun getQueryHistoric(): String {
        return buildQuery(
            "historic" to "ruins",
            "historic" to "monument",
            "historic" to "memorial"
        )
    }

    override fun getQueryTourism(): String {
        return buildQuery(
            "tourism" to "attraction"
        )
    }
}
