package com.example.travelapp.data.model

import com.google.gson.annotations.SerializedName

data class OverpassResponse(
    @SerializedName("elements") val elements: List<Element>
)

data class Element(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: Long,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("tags") val tags: Map<String, String>? = null
)
