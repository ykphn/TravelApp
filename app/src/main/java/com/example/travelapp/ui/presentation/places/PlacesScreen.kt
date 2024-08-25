package com.example.travelapp.ui.presentation.places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travelapp.ui.presentation.places.viewModel.PlacesListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelapp.data.model.Element
import com.example.travelapp.ui.component.DropdownFilterMenu
import com.google.android.gms.maps.model.LatLng

@Composable
fun PlacesListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesListViewModel = hiltViewModel(),
    userLocal: MutableState<LatLng>,
    selectedPlaces: (LatLng) -> Unit
) {

    val tourismPlaces by viewModel.tourismPlaces.collectAsState()
    val historicPlaces by viewModel.historicPlaces.collectAsState()
    val museumAndArcPlaces by viewModel.museumAndArcPlaces.collectAsState()
    val placesType by viewModel.placesType.collectAsState()
    val distance by viewModel.distance.collectAsState()

    val dropdownMenu = remember { mutableStateOf(false) }
    val places = remember { mutableStateOf(tourismPlaces) }

    LaunchedEffect(tourismPlaces, historicPlaces, museumAndArcPlaces) {
        when (placesType) {
            "tourismPlaces" -> places.value = tourismPlaces
            "historicPlaces" -> places.value = historicPlaces
            "museumAndArcPlaces" -> places.value = museumAndArcPlaces
        }
    }

    LaunchedEffect(userLocal) {
        viewModel.userLocalPlaces(userLocal)
    }

    LaunchedEffect(distance, dropdownMenu.value) {
        if (!dropdownMenu.value) {
            viewModel.userLocalPlaces(userLocal)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {
            items(places.value.elements) { response ->
                if (!response.tags?.get("name").isNullOrEmpty()) ListItem(
                    response = response,
                    imagePainter = painterResource(id = places.value.icon!!),
                    selectedPlaces
                )
            }
        }

        if (dropdownMenu.value) {
            DropdownFilterMenu(type = placesType, onSelect = { type ->
                viewModel.setPlacesType(type)
                when (type) {
                    "tourismPlaces" -> {
                        places.value = tourismPlaces
                    }

                    "historicPlaces" -> {
                        places.value = historicPlaces
                    }

                    "museumAndArcPlaces" -> {
                        places.value = museumAndArcPlaces
                    }
                }
            }, distance = distance, selectDistance = { distance ->
                viewModel.setDistance(distance)
            })
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 72.dp, end = 16.dp)
                .height(32.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (dropdownMenu.value) Color.Black else Color.White,
                        shape = if (dropdownMenu.value) RoundedCornerShape(
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        ) else RoundedCornerShape(8.dp)
                    )
                    .clickable { dropdownMenu.value = !dropdownMenu.value },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Filter",
                    color = if (dropdownMenu.value) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
fun ListItem(response: Element, imagePainter: Painter, selectedPlaces: (LatLng) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                if (response.lat != null && response.lon != null)
                    selectedPlaces(LatLng(response.lat, response.lon))
            },
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