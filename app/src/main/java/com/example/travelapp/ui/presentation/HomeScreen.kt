package com.example.travelapp.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelapp.ui.presentation.viewModel.HomeScreenViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()

) {

    val location by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFirstLoading by viewModel.isFirstLoading.collectAsState()
    val latLng = LatLng(location.let { loc -> loc?.latitude } ?: 0.0,
        location.let { it?.longitude } ?: 0.0
    )
    val cameraPositionState = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(latLng, 5f)

    }

    LaunchedEffect(key1 = isLoading) {
        location?.let {
            cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(latLng, 10f)
            ), 1000)

        }

    }

    if (isFirstLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

    } else {
        Scaffold(
            floatingActionButton = {
                Button(
                    onClick = {
                        viewModel.getCurrentLocation()
                    }) {
                    if (isLoading) {
                        Text(text = "Loading")
                        return@Button
                    }
                    Text(text = "Fetch Location")
                }
            },
            floatingActionButtonPosition = FabPosition.Center

        ) { paddingValues ->
            GoogleMap(
                modifier = modifier.padding(paddingValues),
                cameraPositionState = cameraPositionState,
                ) {
                // Add a marker to the center to check if the map is rendering
                Marker(
                    state = rememberMarkerState(
                        position = latLng,
                    ),
                    title = "Center of Map",
                    snippet = "Check if map is loading "
                )


            }

        }

    }


}


