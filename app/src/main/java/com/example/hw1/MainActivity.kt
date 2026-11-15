package com.example.hw1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.hw1.notifications.EXTRA_FROM_NOTIFICATION
import com.example.hw1.notifications.EXTRA_NOTIFICATION_TEXT
import com.example.hw1.notifications.EXTRA_NOTIFICATION_TITLE
import com.example.hw1.notifications.Notification
import com.example.hw1.ui.FirstScreen
import com.example.hw1.ui.PermissionDeniedScreen
import com.example.hw1.ui.SecondScreen
import com.example.hw1.ui.theme.HW1Theme

class MainActivity : ComponentActivity() {
    private val hasNotificationPermission = mutableStateOf(true)
    private val showSecondScreen = mutableStateOf(false)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission.value = granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Notification.createNotificationChannels(this)
        requestNotificationPermissionIfNeeded()
        handleNotificationIntent(intent, showText = false)

        setContent {
            HW1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val contentModifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()

                    when {
                        !hasNotificationPermission.value ->
                            PermissionDeniedScreen(modifier = contentModifier)

                        showSecondScreen.value ->
                            SecondScreen(
                                modifier = contentModifier,
                                onBack = { showSecondScreen.value = false }
                            )

                        else ->
                            FirstScreen(
                                modifier = contentModifier,
                                onOpenSecondScreen = { showSecondScreen.value = true }
                            )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent, showText = true)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            hasNotificationPermission.value = granted
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun handleNotificationIntent(intent: Intent?, showText: Boolean) {
        if (intent?.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false) != true) return
        val title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE).orEmpty()
        val text = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT).orEmpty()
        Toast.makeText(
            this,
            if (showText) text else title,
            Toast.LENGTH_SHORT
        ).show()
        intent.removeExtra(EXTRA_FROM_NOTIFICATION)
    }
}
