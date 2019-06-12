package knaufdan.android.simpletimerapp.util.alarm

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import knaufdan.android.simpletimerapp.util.Constants
import knaufdan.android.simpletimerapp.util.NotificationService
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        sharedPrefService.saveTo(Constants.STATE_KEY, TimerState.FINISH_STATE)

        notificationService.sendTimerFinishedNotification()
    }
}