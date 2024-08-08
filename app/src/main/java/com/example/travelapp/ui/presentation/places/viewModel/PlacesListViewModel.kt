package com.example.travelapp.ui.presentation.places.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.R
import com.example.travelapp.data.model.OverpassQueryResponse
import com.example.travelapp.data.model.OverpassResponse
import com.example.travelapp.data.remote.query.OverpassQueryProviderFactory
import com.example.travelapp.data.remote.repository.OverpassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val overpassRepository: OverpassRepository,
    private val overpassQueryProviderFactory: OverpassQueryProviderFactory
) : ViewModel() {

    private val _places = MutableStateFlow(OverpassResponse(elements = emptyList(), icon = null))
    val places: StateFlow<OverpassResponse> = _places

    init {
        val latitude = 39.9234
        val longitude = 32.8597
        val distance = 5000

        val overpassQueryProvider = overpassQueryProviderFactory.create(latitude, longitude, distance)
        fetchPlaces(overpassQueryProvider.getQueryHistoric())
    }

    private fun fetchPlaces(queryResponse: OverpassQueryResponse) {
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
                    _places.value = response
                    _places.value.icon = queryResponse.image
                }
        }
    }
}
