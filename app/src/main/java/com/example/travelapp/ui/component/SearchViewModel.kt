package com.example.travelapp.ui.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.data.maps.MapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (
    private val mapsRepository: MapsRepository
):ViewModel() {



    fun getPlaces(){
        viewModelScope.launch {
            mapsRepository.searchPlacesByText("izmir").collectLatest { it->
                println(it.data)
            }
        }

    }

}