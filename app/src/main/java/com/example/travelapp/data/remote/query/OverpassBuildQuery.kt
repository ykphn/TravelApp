package com.example.travelapp.data.remote.query

class OverpassBuildQuery {
    fun buildQuery(latitude: Double, longitude: Double, distance: Int, vararg filters: Pair<String, String>): String {
        val filterQueries = filters.joinToString("\n") {
            """node["${it.first}"="${it.second}"](around:$distance,$latitude,$longitude);"""
        }
        return """
        [out:json];
        (
            $filterQueries
        );
        out;
        """.trimIndent()
    }
}