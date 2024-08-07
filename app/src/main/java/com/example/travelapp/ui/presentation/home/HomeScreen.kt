package com.example.travelapp.ui.presentation.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.travelapp.ui.component.LocationCoarsePermissionTextProvider
import com.example.travelapp.ui.component.PermissionDialog
import com.example.travelapp.ui.component.PermissionTextProvider
import com.example.travelapp.ui.presentation.home.viewModel.HomeScreenViewModel
import com.example.travelapp.utility.getActivityOrNull
import com.example.travelapp.utility.hasLocationPermission
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
    viewModel: HomeScreenViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

) {


    val location by viewModel.location.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFirstLoading by viewModel.isFirstLoading.collectAsState()
    val isPermissionGranted by viewModel.isPermissionGranted.collectAsState()
    val dialogQueue by viewModel.visibleDialogQueue.collectAsState()
    val context = LocalContext.current

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
    LaunchedEffect(Unit) {
        multiplePermissions.launch(permissionsToRequest)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (dialogQueue.isNotEmpty()) {
                    if (context.hasLocationPermission()) {
                        dialogQueue.forEach{ _ ->
                            viewModel.dismissDialog()
                        }
                        println("deneme")
                    }else if (!context.hasLocationPermission()){
                        multiplePermissions.launch(permissionsToRequest)
                    }

                }

            }

        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}


