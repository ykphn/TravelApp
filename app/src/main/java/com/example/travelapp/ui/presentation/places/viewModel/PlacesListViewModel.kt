package com.example.travelapp.ui.presentation.places.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.model.OverpassQueryResponse
import com.example.travelapp.data.model.OverpassResponse
import com.example.travelapp.data.remote.query.OverpassQueryProviderFactory
import com.example.travelapp.data.remote.repository.OverpassRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val overpassRepository: OverpassRepository,
    private val overpassQueryProviderFactory: OverpassQueryProviderFactory
) : ViewModel() {

    private val _tourismPlaces =
        MutableStateFlow(OverpassResponse(elements = emptyList(), icon = null))
    val tourismPlaces: StateFlow<OverpassResponse> = _tourismPlaces

    private val _historicPlaces =
        MutableStateFlow(OverpassResponse(elements = emptyList(), icon = null))
    val historicPlaces: StateFlow<OverpassResponse> = _historicPlaces

    private val _museumAndArcPlaces =
        MutableStateFlow(OverpassResponse(elements = emptyList(), icon = null))
    val museumAndArcPlaces: StateFlow<OverpassResponse> = _museumAndArcPlaces

    private val _placesType = MutableStateFlow("tourismPlaces")
    val placesType: StateFlow<String> get() = _placesType

    private val _distance = MutableStateFlow(10000)
    val distance: StateFlow<Int> get() = _distance

    fun setPlacesType(type: String) {
        _placesType.value = type
    }

    fun setDistance(type: Int) {
        _distance.value = type
    }

    fun userLocalPlaces(userLocal: MutableState<LatLng>) {
        val latitude = userLocal.value.latitude
        val longitude = userLocal.value.longitude

        val overpassQueryProvider =
            overpassQueryProviderFactory.create(latitude, longitude, distance.value)
        fetchPlaces(_tourismPlaces, overpassQueryProvider.getQueryTourism())
        fetchPlaces(_historicPlaces, overpassQueryProvider.getQueryHistoric())
        fetchPlaces(_museumAndArcPlaces, overpassQueryProvider.getQueryMuseumAndArchaeological())
    }

    private fun fetchPlaces(
        places: MutableStateFlow<OverpassResponse>,
        queryResponse: OverpassQueryResponse
    ) {
        viewModelScope.launch {
            overpassRepository.getPlaces(queryResponse.query)
                .collect { response ->
                    Log.d("PlacesListViewModel", "Response received: $response")
                    if (response.elements.isEmpty()) {
                        Log.d("PlacesListViewModel", "No elements found")
                    } else {
                        response.elements.forEach {
                            Log.d("PlacesListViewModel", "${it.tags}")
                        }
                    }
                    places.value = response
                    places.value.icon = queryResponse.image
                }
        }
    }

}