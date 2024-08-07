package com.example.travelapp.data.remote.query

interface OverpassQuery {
    fun getQueryMuseumAndArchaeological(): String
    fun getQueryHistoric(): String
    fun getQueryTourism(): String
}