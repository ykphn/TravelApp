package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import com.example.travelapp.data.remote.repository.OverpassRepository
import com.example.travelapp.ui.theme.TravelAppTheme

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var overpassRepository: OverpassRepository

    // StateFlow to hold place data
    private val _places = MutableStateFlow<List<MyPlace>>(emptyList())
    val places: StateFlow<List<MyPlace>> = _places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       

        setContent {
            TravelAppTheme {
                val _places = MutableStateFlow<List<MyPlace>>(emptyList())
                val places: StateFlow<List<MyPlace>> = _places
                val ankara = LatLng(39.9334, 32.8597)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(ankara, 5f)
                }
                MapScreen(cameraPositionState = cameraPositionState, places =places.collectAsState().value )

            }
        }
    }
}

// Define a data class for the place
data class MyPlace(
    val name: String,
    val latLng: LatLng
)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    places: List<MyPlace>
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { LatLeng->
            println(LatLeng)
        }
    ) {
        // Add a marker to the center to check if the map is rendering
        Marker(
            state = rememberMarkerState(position = LatLng(39.9334, 32.8597)),
            title = "Center of Map",
            snippet = "Check if map is loading"
        )

        // Markers for fetched places
        places.forEach { place ->
            Marker(
                state = rememberMarkerState(position = place.latLng),
                title = place.name
            )
        }
    }
}

