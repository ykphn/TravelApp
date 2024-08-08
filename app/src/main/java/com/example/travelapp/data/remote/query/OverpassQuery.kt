package com.example.travelapp.data.remote.query

import com.example.travelapp.data.model.OverpassQueryResponse

interface OverpassQuery {
    fun getQueryMuseumAndArchaeological(): OverpassQueryResponse
    fun getQueryHistoric(): OverpassQueryResponse
    fun getQueryTourism(): OverpassQueryResponse
}