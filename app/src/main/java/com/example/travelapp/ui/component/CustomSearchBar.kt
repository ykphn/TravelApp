package com.example.travelapp.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.volley.BuildConfig

import com.example.travelapp.R
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(modifier: Modifier = Modifier,searchBarState: SearchBarState) {
    val viewModel :SearchViewModel = hiltViewModel()

    SearchBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search,
                contentDescription ="SearchIcon" , modifier = Modifier.clickable {
                    viewModel.getPlaces()
                })
        },
        placeholder = { Text("Venues...", style = MaterialTheme.typography.titleMedium) },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(16.dp)

    ) {


    }

}



enum class SearchBarState {
    SEARCH_IN_MAP,
    SEARCH_IN_LIST
}