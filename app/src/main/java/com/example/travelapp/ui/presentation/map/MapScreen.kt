package com.example.travelapp.ui.presentation.map

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.travelapp.ui.component.CustomSearchBar
import com.example.travelapp.ui.component.LocationCoarsePermissionTextProvider
import com.example.travelapp.ui.component.PermissionDialog
import com.example.travelapp.ui.component.SearchBarState
import com.example.travelapp.ui.presentation.map.viewModel.MapScreenViewModel
import com.example.travelapp.utility.getActivityOrNull
import com.example.travelapp.utility.hasLocationPermission
import com.example.travelapp.utility.openAppSettings
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private val permissionsToRequest = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapScreenViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    selectedPlaces: MutableState<LatLng?>,
    userLocalRollBack: (LatLng) -> Unit
) {
    val location by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFirstLoading by viewModel.isFirstLoading.collectAsState()
    val dialogQueue by viewModel.visibleDialogQueue.collectAsState()
    val context = LocalContext.current
    val searchText by viewModel.searchText.collectAsState()
    val searchBarState by viewModel.searchBarState.collectAsState()
    val placeList by viewModel.placesList.collectAsState()

    val latLng = LatLng(
        location?.latitude ?: 0.0,
        location?.longitude ?: 0.0
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 5f)
    }

    val markerState = rememberMarkerState(position = selectedPlaces.value ?: latLng)

    val multiplePermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    ObserverLifecycleEvent(
        lifecycleOwner = lifecycleOwner,
        onResumeEvent = {
            if (context.hasLocationPermission()) {
                dialogQueue.forEach { _ ->
                    viewModel.dismissDialog()
                }
            }
        },
        onStartEvent = {
            if (!context.hasLocationPermission()) {
                if (!shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && !shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    multiplePermissions.launch(permissionsToRequest)
                }
            }
        },
        onCreateEvent = {
            if (!context.hasLocationPermission()) {
                multiplePermissions.launch(permissionsToRequest)
            }
        },
    )

    dialogQueue.forEach { perms ->
        PermissionDialog(
            permissionTextProvider = LocationCoarsePermissionTextProvider(),
            onDismiss = {
                viewModel.onPermissionResult(permission = perms, isGranted = false)
            },
            onGoToAppSettingsClick = {
                context.getActivityOrNull()?.openAppSettings()
            }
        )
    }

    LaunchedEffect(key1 = isLoading, key2 = location) {
        location?.let {
            val newLatLng = LatLng(it.latitude, it.longitude)
            markerState.position = newLatLng
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(newLatLng, 10f)
                ), 1000
            )
            userLocalRollBack(newLatLng)
        }
    }

    LaunchedEffect(selectedPlaces) {
        selectedPlaces.value?.let {
            userLocalRollBack(it)
            markerState.position = it
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(it, 15f)
            )
            cameraPositionState.animate(cameraUpdate, 1000)
        }
    }

    if (isFirstLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            floatingActionButton = {
                Button(
                    onClick = {
                        viewModel.getCurrentLocation()
                    }) {
                    if (isLoading) {
                        Text(text = "Loading")
                        return@Button
                    }
                    Text(text = "Fetch Location")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->

            CustomSearchBar(
                searchBarState = SearchBarState.SEARCH_IN_MAP,
                onQueryChange = {
                    viewModel.setSearchText(it)
                },
                onSearch = {
                    viewModel.getPlacesByString(it)
                },
                onActiveChange = {
                    viewModel.setSearchBarState(it)
                },
                isActive = searchBarState,
                searchText = searchText,
                placeList = placeList
            )

            GoogleMap(
                modifier = modifier.padding(paddingValues),
                cameraPositionState = cameraPositionState,
            ) {
                Marker(
                    state = markerState,
                    title = "Selected Place",
                    snippet = "This is the selected place"
                )
            }
        }
    }
}

@Composable
fun ObserverLifecycleEvent(
    lifecycleOwner: LifecycleOwner,
    onCreateEvent: () -> Unit = {},
    onStartEvent: () -> Unit = {},
    onResumeEvent: () -> Unit = {},
    onPauseEvent: () -> Unit = {},
    onStopEvent: () -> Unit = {},
    onDestroyEvent: () -> Unit = {}
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> onCreateEvent()
                Lifecycle.Event.ON_START -> onStartEvent()
                Lifecycle.Event.ON_RESUME -> onResumeEvent()
                Lifecycle.Event.ON_PAUSE -> onPauseEvent()
                Lifecycle.Event.ON_STOP -> onStopEvent()
                Lifecycle.Event.ON_DESTROY -> onDestroyEvent()
                Lifecycle.Event.ON_ANY -> {} // This case is typically not handled
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}