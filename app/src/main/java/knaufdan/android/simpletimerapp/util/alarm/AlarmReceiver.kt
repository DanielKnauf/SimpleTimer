package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import knaufdan.android.simpletimerapp.util.Constants
import knaufdan.android.simpletimerapp.util.Constants.END_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.PAUSE_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.STATE_KEY
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

        if (intent.hasExtra(IS_ON_REPEAT)
            && intent.getBooleanExtra(IS_ON_REPEAT, false)
            && intent.hasExtra(END_TIME_KEY)
        ) {
            sharedPrefService.saveTo(PAUSE_TIME_KEY, Date().time)
            sharedPrefService.saveTo(STATE_KEY, TimerState.RESTARTED_IN_BACKGROUND)

            val endTime = intent.getIntExtra(END_TIME_KEY, 0)

            alarmService.setAlarm(endTime.toLong(), intent.extras)

            notificationService.sendTimerRestartNotification()
        } else {
            sharedPrefService.saveTo(Constants.STATE_KEY, TimerState.FINISH_STATE)

            notificationService.sendTimerFinishedNotification()
        }
    }
}