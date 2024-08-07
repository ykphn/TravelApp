package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.travelapp.data.remote.repository.OverpassRepository
import com.example.travelapp.ui.presentation.home.MapScreen
import com.example.travelapp.ui.theme.TravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var overpassRepository: OverpassRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



       

        setContent {
            TravelAppTheme {


                MapScreen()
            }
        }
    }
}





