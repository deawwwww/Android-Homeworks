package com.example.hw1.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.hw1.R

@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var coordinatesText by rememberSaveable {
        mutableStateOf(context.getString(R.string.coordinates_instruction))
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            coordinatesText = context.getString(R.string.location_permission_required)
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(coordinatesText)
        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED ->
                        coordinatesText = getCoordinates(context)

                    else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.get_coordinates))
        }
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

private fun getCoordinates(context: Context): String {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val provider = locationManager.getBestProvider(Criteria(), false)
        ?: return context.getString(R.string.coordinates_unavailable)
    val location = locationManager.getLastKnownLocation(provider)
    return if (location != null) {
        context.getString(
            R.string.coordinates_format,
            location.latitude,
            location.longitude
        )
    } else {
        context.getString(R.string.coordinates_unavailable_enable_location)
    }
}
