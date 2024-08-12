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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    userLocalRollBack: (LatLng) -> Unit
) {


    val location by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFirstLoading by viewModel.isFirstLoading.collectAsState()
    val isPermissionGranted by viewModel.isPermissionGranted.collectAsState()
    val dialogQueue by viewModel.visibleDialogQueue.collectAsState()
    val context = LocalContext.current

    var checkPermissionOneTime = remember {
        mutableStateOf(true)
    }

    val latLng = LatLng(location.let { loc -> loc?.latitude } ?: 0.0,
        location.let { it?.longitude } ?: 0.0
    )
    val cameraPositionState = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(latLng, 5f)

    }
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
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(latLng, 10f)
                ), 1000
            )
            userLocalRollBack(latLng)
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
            CustomSearchBar(searchBarState = SearchBarState.SEARCH_IN_MAP)
            GoogleMap(
                modifier = modifier.padding(paddingValues),
                cameraPositionState = cameraPositionState,
            ) {
                // Add a marker to the center to check if the map is rendering
                Marker(
                    state = rememberMarkerState(
                        position = latLng,
                    ),
                    title = "Center of Map",
                    snippet = "Check if map is loading "
                )


            }

        }

    }


}

@Composable
fun ObserverLifecycleEvent(
    modifier: Modifier = Modifier,
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




