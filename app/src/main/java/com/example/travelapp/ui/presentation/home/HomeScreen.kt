package com.example.travelapp.ui.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travelapp.ui.presentation.map.MapScreen
import com.example.travelapp.ui.presentation.places.PlacesListScreen

@Composable
fun MainScreen() {
    val selectedButton = remember { mutableStateOf("Map") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {

        Crossfade(targetState = selectedButton.value, label = "Screen Changer") { screen ->
            when (screen) {
                "Map" -> MapScreen()
                "List" -> PlacesListScreen()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .height(48.dp)
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (selectedButton.value == "Map") Color.Black else Color.White,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp,
                            topEnd = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .clickable { selectedButton.value = "Map" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Map",
                    color = if (selectedButton.value == "Map") Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (selectedButton.value == "List") Color.Black else Color.White,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            bottomStart = 24.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .clickable { selectedButton.value = "List" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "List",
                    color = if (selectedButton.value == "List") Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}



