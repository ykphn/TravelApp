package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelapp.data.remote.repository.OverpassRepository
import com.example.travelapp.ui.theme.TravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var overpassRepository: OverpassRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAppTheme {

            }
        }


        // Api veri çekme
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = overpassRepository.getPlaces("node[amenity=restaurant](50.7,7.1,50.8,7.2);out;")
                response.elements.forEach {
                    println("Place: ${it.tags?.get("name") ?: "Unnamed place"} (lat: ${it.lat}, lon: ${it.lon})")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}