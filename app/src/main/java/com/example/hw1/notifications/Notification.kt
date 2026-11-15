package com.example.hw1.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.example.hw1.MainActivity
import com.example.hw1.R

const val EXTRA_FROM_NOTIFICATION = "extra_from_notification"
const val EXTRA_NOTIFICATION_TITLE = "extra_notification_title"
const val EXTRA_NOTIFICATION_TEXT = "extra_notification_text"

enum class NotificationImportance(
    @StringRes val labelRes: Int,
    val channelId: String
) {
    MIN(R.string.importance_min, "channel_importance_min"),
    DEFAULT(R.string.importance_default, "channel_importance_default"),
    HIGH(R.string.importance_high, "channel_importance_high"),
}

object Notification {
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getNotificationManager(context)
            NotificationImportance.entries.forEach { importance ->
                val channelImportance = when (importance) {
                    NotificationImportance.MIN -> NotificationManager.IMPORTANCE_MIN
                    NotificationImportance.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                    NotificationImportance.HIGH -> NotificationManager.IMPORTANCE_HIGH
                }
                notificationManager.createNotificationChannel(
                    NotificationChannel(
                        importance.channelId,
                        context.getString(importance.labelRes),
                        channelImportance
                    )
                )
            }
        }
    }

    fun showNotification(
        context: Context,
        id: Int,
        title: String,
        text: String,
        isNonCancellable: Boolean,
        importance: NotificationImportance,
    ) {
        val notification = buildNotification(context, title, text, isNonCancellable, importance)
        getNotificationManager(context).notify(id, notification)
    }

    fun hasActiveNotifications(context: Context): Boolean =
        getNotificationManager(context).activeNotifications.isNotEmpty()

    fun isNotificationActive(context: Context, id: Int): Boolean =
        getNotificationManager(context).activeNotifications.any { it.id == id }

    fun deleteAllNotifications(context: Context) {
        getNotificationManager(context).cancelAll()
    }

    private fun buildNotification(
        context: Context,
        title: String,
        text: String,
        isNonCancellable: Boolean,
        importance: NotificationImportance,
    ): Notification {
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_FROM_NOTIFICATION, true)
            putExtra(EXTRA_NOTIFICATION_TITLE, title)
            putExtra(EXTRA_NOTIFICATION_TEXT, text)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, importance.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setOngoing(isNonCancellable)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun getNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
