package com.example.travelapp.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.places.api.model.Place


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    searchBarState: SearchBarState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    isActive: Boolean,
    searchText: String,
    placeList: List<Place>?
) {
    val viewModel :SearchViewModel = hiltViewModel()

    SearchBar(
        query = searchText,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isActive,
        onActiveChange = onActiveChange,
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

        placeList?.let { idx->
            idx.forEach{ response ->
                Card {
                    response.name?.let { Text(text = it) }
                }
            }
        }

    }

}



enum class SearchBarState {
    SEARCH_IN_MAP,
    SEARCH_IN_LIST
}