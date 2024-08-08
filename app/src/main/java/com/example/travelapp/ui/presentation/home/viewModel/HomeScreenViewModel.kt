package com.example.travelapp.ui.presentation.home.viewModel

import android.location.Location
import androidx.compose.runtime.LaunchedEffect
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


    private val _visibleDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visibleDialogQueue = _visibleDialogQueue.asStateFlow()

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

    private val _isPermissionGranted = MutableStateFlow<Boolean>(false)
    val isPermissionGranted = _isPermissionGranted.asStateFlow()

    init {

        getLastLocation()
    }

    fun getCurrentLocation() {
        viewModelScope.launch {


            locationManager.getCurrentLocation().collectLatest { state ->
                when (state) {


                    is ScreenState.Error -> _isPermissionGranted.update { false }
                    is ScreenState.Loading -> _isLoading.update { true }
                    is ScreenState.Success -> {

                        println(state.data)

                        _location.update { state.data }
                        _isLoading.update { false }
                        _isFirstLoading.update { false }

                    }
                }
            }
        }
    }

    fun getLastLocation() {
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
                        println(" ${_isFirstLoading.value}  ****")

                    }

                }

            }
        }
    }


}