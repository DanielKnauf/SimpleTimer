package knaufdan.android.simpletimerapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.CATEGORY_ALARM
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    private val contextProvider: ContextProvider,
    private val textProvider: TextProvider
) {

    private val channelId = "knaufdan.android.simpletimerapp.alarm"

    private val notificationManager =
        contextProvider.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendTimerFinishedNotification() {
        sendNotification(R.string.timer_finished_notification_content_text)
    }

    fun sendTimerRestartNotification(){
        sendNotification(R.string.timer_restart_notification_content_text)
    }

    private fun sendNotification(@StringRes text : Int) {
        createNotificationChannel()

        val notification = contextProvider.context.buildNotification(text)

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notificationManager.getNotificationChannel(channelId) == null
        ) {
            val name = textProvider.getText(R.string.timer_finished_notification_channel_name)
            val description = textProvider.getText(R.string.timer_finished_notification_channel_description)

            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
                .apply { this.description = description }
                .apply { enableVibration(true) }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun Context.buildNotification(@StringRes text: Int): Notification =
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_important)
            .setContentTitle(textProvider.getText(R.string.timer_finished_notification_title))
            .setContentText(textProvider.getText(text))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setCategory(CATEGORY_ALARM)
            .setVibrate(longArrayOf())
            .setContentIntent(createIntentToStartApp())
            .build()

    private fun Context.createIntentToStartApp(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        return PendingIntent.getActivity(this, 0, intent, 0)
    }
}
