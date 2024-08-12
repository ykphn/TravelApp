package com.example.travelapp.data.maps

import com.example.travelapp.utility.ScreenState
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class MapsRepositoryImpl(
    private val placesClient: PlacesClient
) : MapsRepository {
    override suspend fun searchPlacesByText(query: String): Flow<ScreenState<List<Place>>> {
        return flow {
            try {
                val placeFields: List<Place.Field> = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,
                )
                val searchByTextRequest =
                    SearchByTextRequest.builder(query, placeFields).setMaxResultCount(10).build()
                emit(ScreenState.Loading())

                placesClient.searchByText(searchByTextRequest).await().let {

                    emit(ScreenState.Success(it.places))
                }

            } catch (e: Exception) {
                println(e.message)
                emit(ScreenState.Error(message = e.message.toString(), boolean = false))
            }


        }
    }
}