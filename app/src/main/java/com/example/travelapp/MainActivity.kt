package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

        val quarry = """
        [out:json];
        (
          node["tourism"="museum"](around:5000,38.4192,27.1287);
        );
        out;
    """.trimIndent()


        // Api veri çekme
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = overpassRepository.getPlaces(quarry)
                response.elements.forEach {
                    println("Place: ${it.tags?.get("name") ?: "Unnamed place"} (lat: ${it.lat}, lon: ${it.lon})")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}