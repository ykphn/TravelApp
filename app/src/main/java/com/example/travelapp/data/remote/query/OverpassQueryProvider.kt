package com.example.travelapp.data.remote.query

import com.example.travelapp.R
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
        ), R.drawable.icon_museum)
    }

    override fun getQueryHistoric(): OverpassQueryResponse {
        return OverpassQueryResponse(buildQuery(
            "historic" to "ruins",
            "historic" to "monument",
            "historic" to "memorial"
        ), R.drawable.icon_historic)
    }

    override fun getQueryTourism(): OverpassQueryResponse {
        return OverpassQueryResponse(buildQuery(
            "tourism" to "attraction"
        ),R.drawable.icon_tourism)
    }
}
