package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import java.util.Date
import javax.inject.Inject
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.TextProvider
import knaufdan.android.core.alarm.AlarmService
import knaufdan.android.core.notification.NotificationService
import knaufdan.android.core.notification.NotificationStyle
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.MainActivity
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState

class AlarmReceiver : DaggerBroadcastReceiver() {
    @Inject
    lateinit var alarmService: AlarmService
    @Inject
    lateinit var notificationService: NotificationService
    @Inject
    lateinit var sharedPrefService: SharedPrefService
    @Inject
    lateinit var textProvider: TextProvider

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
            sharedPrefService.saveTo(KEY_PAUSE_TIME, Date().time)
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESTARTED_IN_BACKGROUND)

            alarmService.setAlarm(
                timeToWakeFromNow = endTime.toLong(),
                extras = intent.extras,
                broadcastReceiverType = this::class.java
            )

            sendNotification(notificationStyle = timerRestartStyle)
        } else {
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.FINISH_STATE)
            sendNotification(notificationStyle = timerFinishStyle)
        }
    }

    private fun NotificationService.configureService() =
        with(textProvider) {
            this@configureService.configure {
                setNotificationChannel(
                    channelId = getText(R.string.timer_finished_notification_channel_id),
                    channelName = getText(R.string.timer_finished_notification_channel_name),
                    channelDescription = getText(R.string.timer_finished_notification_channel_description),
                    channelImportance = 4
                )

                setVibration(enabled = true)

                setAutoCancel(enabled = true)
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
