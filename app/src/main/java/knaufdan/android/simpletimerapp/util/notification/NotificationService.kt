package knaufdan.android.simpletimerapp.util.notification

import android.app.Notification
import android.app.Notification.CATEGORY_ALARM
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import javax.inject.Inject
import javax.inject.Singleton
import knaufdan.android.simpletimerapp.ui.MainActivity
import knaufdan.android.simpletimerapp.util.ContextProvider
import knaufdan.android.simpletimerapp.util.TextProvider

@Singleton
class NotificationService @Inject constructor(
    private val contextProvider: ContextProvider,
    private val textProvider: TextProvider
) : INotificationService {
    private val notificationManager by lazy {
        contextProvider.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun configure(adjust: INotificationServiceConfig.() -> Unit) = adjust(config)

    override fun sendNotification(notificationStyle: NotificationStyle) {
        if (!config.isValid()) {
            Log.e(
                this::class.simpleName,
                "Current NotificationServiceConfig is not valid, no notification could be shown : $config"
            )
            return
        }
        createNotificationChannel()

        contextProvider.context.buildNotification(notificationStyle).apply {
            notificationManager.notify(
                System.currentTimeMillis().toInt(),
                this
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel(config.channelId) == null
        ) {
            NotificationChannel(
                config.channelId,
                config.channelName,
                config.channelImportance
            ).apply {
                this.description = config.channelDescription
                enableVibration(config.isVibrationEnabled)
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    private fun Context.buildNotification(notificationStyle: NotificationStyle): Notification =
        NotificationCompat.Builder(this, config.channelId)
            .setSmallIcon(notificationStyle.smallIcon)
            .setContentTitle(textProvider.getText(notificationStyle.title))
            .setContentText(textProvider.getText(notificationStyle.text))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(config.isAutoCancelEnabled)
            .setCategory(CATEGORY_ALARM)
            .setVibrate(longArrayOf())
            .setContentIntent(createIntentToStartApp())
            .build()

    companion object {
        private val config: NotificationServiceConfig = NotificationServiceConfig.EMPTY

        private fun Context.createIntentToStartApp(): PendingIntent {
            val intent = Intent(this, MainActivity::class.java)
                .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            return PendingIntent.getActivity(this, 0, intent, 0)
        }
    }
}
