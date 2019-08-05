package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_END_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.NotificationService
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.service.TimerState
import java.util.*
import javax.inject.Inject

class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    @Inject
    lateinit var alarmService: AlarmService

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.hasExtra(KEY_IS_ON_REPEAT)
            && intent.getBooleanExtra(KEY_IS_ON_REPEAT, false)
            && intent.hasExtra(KEY_CURRENT_END_TIME)
        ) {
            sharedPrefService.saveTo(KEY_PAUSE_TIME, Date().time)
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESTARTED_IN_BACKGROUND)

            val endTime = intent.getIntExtra(KEY_CURRENT_END_TIME, 0)

            alarmService.setAlarm(endTime.toLong(), intent.extras)

            notificationService.sendTimerRestartNotification()
        } else {
            sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.FINISH_STATE)

            notificationService.sendTimerFinishedNotification()
        }
    }
}