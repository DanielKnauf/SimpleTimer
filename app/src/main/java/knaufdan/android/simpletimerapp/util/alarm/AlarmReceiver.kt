package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.NotificationService
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.service.TimerState
import java.util.Date
import javax.inject.Inject

class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    @Inject
    lateinit var alarmService: AlarmService

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        super.onReceive(context, intent)

        val endTime = intent.getIntExtra(KEY_CURRENT_MAXIMUM, 0)

        if (intent.getBooleanExtra(KEY_IS_ON_REPEAT, false)
            && endTime > 0
        ) {
            sharedPrefService.saveTo(KEY_PAUSE_TIME, Date().time)
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESTARTED_IN_BACKGROUND)

            alarmService.setAlarm(
                timeToWakeFromNow = endTime.toLong(),
                extras = intent.extras,
                broadcastReceiverType = this::class.java
            )

            notificationService.sendTimerRestartNotification()
        } else {
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.FINISH_STATE)

            notificationService.sendTimerFinishedNotification()
        }
    }
}