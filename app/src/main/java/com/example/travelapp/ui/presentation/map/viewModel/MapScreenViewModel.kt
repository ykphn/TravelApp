package com.example.travelapp.ui.presentation.map.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.location.api.LocationManager
import com.example.travelapp.data.maps.MapsRepository
import com.example.travelapp.utility.ScreenState
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val mapsRepository: MapsRepository
) : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private val _isFirstLoading = MutableStateFlow(true)
    val isFirstLoading = _isFirstLoading.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _visibleDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visibleDialogQueue = _visibleDialogQueue.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchBarState = MutableStateFlow(false)
    val searchBarState = _searchBarState.asStateFlow()

    private val _placesList = MutableStateFlow<List<Place>?>(null)
    val placesList = _placesList.asStateFlow()

    private val _isPermissionGranted = MutableStateFlow(false)

    init {

        getLastLocation()
    }

    fun setSearchBarState(state: Boolean) {
        _searchBarState.value = state
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun dismissDialog() {

        _visibleDialogQueue.update { it.dropLast(1) }
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted) {
            _visibleDialogQueue.update { queue ->
                if (!queue.contains(permission)) queue + permission else queue
            }
        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            locationManager.getCurrentLocation().collectLatest { state ->
                when (state) {
                    is ScreenState.Error -> _isPermissionGranted.update { false }
                    is ScreenState.Loading -> _isLoading.update { true }
                    is ScreenState.Success -> {
                        _location.update { state.data }
                        _isLoading.update { false }
                        _isFirstLoading.update { false }
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        viewModelScope.launch {
            locationManager.getLastLocation().collectLatest { state ->
                when (state) {
                    is ScreenState.Error -> {
                        _isPermissionGranted.update { false }
                        _isFirstLoading.update { false }
                    }
                    is ScreenState.Loading -> {
                        _isLoading.update { true }
                    }
                    is ScreenState.Success -> {
                        _location.update { state.data }
                        _isLoading.update { false }
                        _isFirstLoading.update { false }
                    }
                }
            }
        }
    }

    fun getPlacesByString(query:String){
        viewModelScope.launch {
           mapsRepository.searchPlacesByText(query).collectLatest { screenState->
               when(screenState){
                   is ScreenState.Error -> Log.d("MapScreenViewModel", "Error")
                   is ScreenState.Loading -> Log.d("MapScreenViewModel", "Loading")
                   is ScreenState.Success ->  {
                          println(screenState.data)

                       _placesList.update { screenState.data }

                   }
               }
           }
        }
    }

}