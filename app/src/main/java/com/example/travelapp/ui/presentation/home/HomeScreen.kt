package com.example.travelapp.ui.presentation.home
import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelapp.ui.component.LocationCoarsePermissionTextProvider
import com.example.travelapp.ui.component.LocationFinePermissionTextProvider
import com.example.travelapp.ui.component.PermissionDialog
import com.example.travelapp.ui.presentation.home.viewModel.HomeScreenViewModel
import com.example.travelapp.utility.openAppSettings
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState



private val permissionsToRequest = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),


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
    when{
        isPermissionGranted -> {
            Text(text = "Permission Granted")
        }
        shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) -> {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    context.openAppSettings()
                }) {
                    Text(text = "Go to App Settings")
                }
            }
        }
        shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) -> {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    context.openAppSettings()
                }) {
                    Text(text = "Go to App Settings")
                }
            }

        }
        else-> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    println(shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION))
                    multiplePermissions.launch(permissionsToRequest)
                }) {
                    Text(text = "Request Permission")
                }
            }

          }
    }

    if (!isPermissionGranted){
        if (!shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION)){

        }else{


        }




//        dialogQueue.forEach{
//                permission-> PermissionDialog(
//            permissionTextProvider = when (permission) {
//                Manifest.permission.ACCESS_FINE_LOCATION -> LocationFinePermissionTextProvider()
//                Manifest.permission.ACCESS_COARSE_LOCATION -> LocationCoarsePermissionTextProvider()
//                else -> return@forEach
//            },
//            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(context as Activity, permission),
//            onDismiss = viewModel::dismissDialog,
//            onOkClick = {
//                viewModel.dismissDialog()
//                multiplePermissions.launch(permissionsToRequest)
//
//
//
//            },
//            onGoToAppSettingsClick = {
//                context.openAppSettings()
//            }
//        )
//
//        }

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






//    if (isFirstLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center,
//        ) {
//            CircularProgressIndicator()
//        }
//
//    } else {
//        Scaffold(
//            floatingActionButton = {
//                Button(
//                    onClick = {
//                        viewModel.getCurrentLocation()
//                    }) {
//                    if (isLoading) {
//                        Text(text = "Loading")
//                        return@Button
//                    }
//                    Text(text = "Fetch Location")
//                }
//            },
//            floatingActionButtonPosition = FabPosition.Center
//
//        ) { paddingValues ->
//            GoogleMap(
//                modifier = modifier.padding(paddingValues),
//                cameraPositionState = cameraPositionState,
//                ) {
//                // Add a marker to the center to check if the map is rendering
//                Marker(
//                    state = rememberMarkerState(
//                        position = latLng,
//                    ),
//                    title = "Center of Map",
//                    snippet = "Check if map is loading "
//                )
//
//
//            }
//
//        }
//
//    }




}


