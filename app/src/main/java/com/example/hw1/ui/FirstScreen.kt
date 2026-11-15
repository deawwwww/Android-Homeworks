package com.example.hw1.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.hw1.R
import com.example.hw1.notifications.Notification
import com.example.hw1.notifications.NotificationImportance

@Composable
fun FirstScreen(
    onOpenSecondScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var text by rememberSaveable { mutableStateOf("") }
    var isNonCancellable by rememberSaveable { mutableStateOf(false) }
    var importance by rememberSaveable { mutableStateOf(NotificationImportance.DEFAULT) }
    var notificationIdInput by rememberSaveable { mutableStateOf("") }
    var hasActiveNotifications by remember {
        mutableStateOf(Notification.hasActiveNotifications(context))
    }
    val lastGeneratedId = remember { mutableIntStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasActiveNotifications = Notification.hasActiveNotifications(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.notification_title_label)) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.notification_text_label)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isNonCancellable,
                onCheckedChange = { isNonCancellable = it },
            )
            Text(stringResource(R.string.non_cancellable))
        }

        Text(
            stringResource(R.string.importance_label),
            style = MaterialTheme.typography.titleSmall
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NotificationImportance.entries.forEach { item ->
                if (item == importance) {
                    Button(
                        onClick = { importance = item },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(item.labelRes))
                    }
                } else {
                    OutlinedButton(
                        onClick = { importance = item },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(item.labelRes))
                    }
                }
            }
        }

        Button(
            onClick = {
                lastGeneratedId.intValue += 1
                Notification.showNotification(
                    context = context,
                    id = lastGeneratedId.intValue,
                    title = title,
                    text = text,
                    isNonCancellable = isNonCancellable,
                    importance = importance,
                )
                hasActiveNotifications = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.send_notification))
        }
        if (lastGeneratedId.intValue > 0) {
            Text(stringResource(R.string.last_notification_id, lastGeneratedId.intValue))
        }

        Text(
            text = stringResource(R.string.active_notifications_management),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 8.dp),
        )
        OutlinedTextField(
            value = notificationIdInput,
            onValueChange = { input -> notificationIdInput = input.filter { it.isDigit() } },
            enabled = hasActiveNotifications,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                enabled = hasActiveNotifications && notificationIdInput.isNotBlank(),
                onClick = {
                    val id = notificationIdInput.toInt()
                    if (Notification.isNotificationActive(context, id)) {
                        Notification.showNotification(
                            context = context,
                            id = id,
                            title = title,
                            text = text,
                            isNonCancellable = isNonCancellable,
                            importance = importance,
                        )
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_active_notification, id),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.edit))
            }
            Button(
                enabled = hasActiveNotifications,
                onClick = {
                    Notification.deleteAllNotifications(context)
                    hasActiveNotifications = false
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.delete_all))
            }
        }

        Button(
            onClick = onOpenSecondScreen,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.second_screen))
        }
    }
}
