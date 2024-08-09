package com.example.travelapp.data.remote.query

import com.example.travelapp.data.model.Icon
import com.example.travelapp.data.model.OverpassQueryResponse
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

    override fun getQueryMuseumAndArchaeological(): OverpassQueryResponse {
        return OverpassQueryResponse(buildQuery(
            "tourism" to "museum",
            "historic" to "archaeological_site"
        ), Icon.museum)
    }

    override fun getQueryHistoric(): OverpassQueryResponse {
        return OverpassQueryResponse(buildQuery(
            "historic" to "ruins",
            "historic" to "monument",
            "historic" to "memorial"
        ), Icon.historic)
    }

    override fun getQueryTourism(): OverpassQueryResponse {
        return OverpassQueryResponse(buildQuery(
            "tourism" to "attraction"
        ), Icon.tourism)
    }
}
