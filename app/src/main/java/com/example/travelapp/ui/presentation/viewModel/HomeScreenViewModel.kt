package com.example.travelapp.ui.presentation.viewModel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.location.api.LocationManager
import com.example.travelapp.utility.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val locationManager: LocationManager
) : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private val _isFirstLoading = MutableStateFlow<Boolean>(true)
    val isFirstLoading = _isFirstLoading.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {

        getLastLocation()


    }

    fun getCurrentLocation() {
        viewModelScope.launch {

            locationManager.getCurrentLocation().collectLatest { state ->
                when (state) {


                    is ScreenState.Error -> println("hata var")
                    is ScreenState.Loading -> _isLoading.update { true }
                    is ScreenState.Success -> {

                        println(state.data)
                        _location.update { state.data }
                        _isLoading.update { false }

                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        viewModelScope.launch {

            locationManager.getLastLocation().collectLatest { state ->
                when (state) {
                    is ScreenState.Error -> println("hatavar")
                    is ScreenState.Loading -> _isLoading.update { true }
                    is ScreenState.Success -> {

                        println(" ${state.data}  ****")
                        _location.update { state.data }
                        _isLoading.update { false }
                        _isFirstLoading.update { false }

                    }

                }

            }
        }
    }


}