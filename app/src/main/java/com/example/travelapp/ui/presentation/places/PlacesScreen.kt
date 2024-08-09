package com.example.travelapp.ui.presentation.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.travelapp.ui.presentation.places.viewModel.PlacesListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelapp.data.model.Element

@Composable
fun PlacesListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesListViewModel = hiltViewModel()
) {
    val tourismPlaces by viewModel.tourismPlaces.collectAsState()
    val historicPlaces by viewModel.historicPlaces.collectAsState()
    val museumAndArcPlaces by viewModel.museumAndArcPlaces.collectAsState()

    val places = remember { mutableStateOf(tourismPlaces) }

    LaunchedEffect(tourismPlaces, historicPlaces, museumAndArcPlaces) {
        places.value = tourismPlaces
    }

    Box(modifier = modifier
        .fillMaxSize()
    ) {
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp)) {
            items(places.value.elements) { response ->
                if (!response.tags?.get("name").isNullOrEmpty()) ListItem(response = response, imagePainter = painterResource(id = places.value.icon!!))
            }
        }
    }

}

@Composable
fun ListItem(response: Element, imagePainter: Painter) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Item Image",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = response.tags!!["name"]!!,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
