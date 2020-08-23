package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import java.util.Date
import javax.inject.Inject
import knaufdan.android.core.alarm.IAlarmService
import knaufdan.android.core.preferences.ISharedPrefService
import knaufdan.android.core.resources.IResourceProvider
import knaufdan.android.services.userinteraction.notification.INotificationService
import knaufdan.android.services.userinteraction.notification.NotificationStyle
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.MainActivity
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState

class AlarmReceiver : DaggerBroadcastReceiver() {
    @Inject
    lateinit var alarmService: IAlarmService

    @Inject
    lateinit var notificationService: INotificationService

    @Inject
    lateinit var sharedPrefService: ISharedPrefService

    @Inject
    lateinit var resourceProvider: IResourceProvider

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        super.onReceive(context, intent)

        notificationService.configureService()

        val endTime = intent.getIntExtra(KEY_CURRENT_MAXIMUM, 0)

        if (intent.getBooleanExtra(KEY_IS_ON_REPEAT, false) &&
            endTime > 0
        ) {
            sharedPrefService.put(KEY_PAUSE_TIME, Date().time)
            sharedPrefService.put(KEY_TIMER_STATE, TimerState.RESTARTED_IN_BACKGROUND)

            alarmService.setAlarm(
                timeToWakeFromNow = endTime.toLong(),
                extras = intent.extras,
                broadcastReceiverType = this::class.java
            )

            sendNotification(timerRestartStyle)
        } else {
            sharedPrefService.put(KEY_TIMER_STATE, TimerState.FINISH_STATE)
            sendNotification(timerFinishStyle)
        }
    }

    private fun INotificationService.configureService() =
        with(resourceProvider) {
            this@configureService.configure {
                setNotificationChannel(
                    channelId = getString(R.string.timer_finished_notification_channel_id),
                    channelName = getString(R.string.timer_finished_notification_channel_name),
                    channelDescription = getString(R.string.timer_finished_notification_channel_description),
                    channelImportance = 4
                )

                setVibration(true)

                setAutoCancel(true)
            }
        }

    private fun sendNotification(notificationStyle: NotificationStyle) {
        notificationService.sendNotification(
            notificationStyle = notificationStyle,
            targetClass = MainActivity::class
        )
    }

    companion object {
        private val timerFinishStyle: NotificationStyle by lazy {
            NotificationStyle(
                text = R.string.timer_finished_notification_content_text,
                title = R.string.timer_notification_title,
                smallIcon = R.drawable.notification_important
            )
        }

        private val timerRestartStyle: NotificationStyle by lazy {
            NotificationStyle(
                text = R.string.timer_restart_notification_content_text,
                title = R.string.timer_notification_title,
                smallIcon = R.drawable.notification_important
            )
        }
    }
}
