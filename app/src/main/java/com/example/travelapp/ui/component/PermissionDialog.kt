package com.example.travelapp.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    onDismiss: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text("Location Permission Required") },
        text = {
            Text(permissionTextProvider.getDescription(true))
        },
        confirmButton = {
            Button(onClick = {
                onGoToAppSettingsClick()
            }) {
                Text("Go to app settings")
            }
        }
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationCoarsePermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined location permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your location for proper functionality."
        }
    }
}

