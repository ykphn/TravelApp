package com.example.travelapp.ui.presentation

import com.example.travelapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.travelapp.ui.presentation.viewModel.PlacesListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelapp.data.model.Element

@Composable
fun PlacesListScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesListViewModel = hiltViewModel()
) {
    val places by viewModel.places.collectAsState()
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(places.elements) { response ->
            ListItem(response = response, imagePainter = painterResource(id = R.drawable.icon_museum))
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
            text = response.tags?.get("name") ?: "Unknown",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
