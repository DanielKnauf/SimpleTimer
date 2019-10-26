package knaufdan.android.simpletimerapp.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.android.AndroidInjection
import java.util.Timer
import javax.inject.Inject
import knaufdan.android.simpletimerapp.util.Constants.KEY_ADJUSTED_PROGRESS
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_LINEAR_INCREMENT
import knaufdan.android.simpletimerapp.util.Constants.MINUTE_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS

class TimerService @Inject constructor() : Service() {

    private val manager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this@TimerService.applicationContext)
    }

    private var timer: Timer? = null

    private var currentTime = 0
    private var endTime: Int = -1

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        endTime = intent.getIntExtra(KEY_CURRENT_MAXIMUM, MINUTE_IN_MILLIS)
        currentTime = intent.getIntExtra(KEY_ADJUSTED_PROGRESS, DEFAULT_START_TIME)
        startTimerRunnable()
        return START_STICKY
    }

    fun sendUpdate() {
        Intent()
            .apply {
                action = if (endTime <= currentTime) TimerAction.FINISH.name else TimerAction.INCREASE.name
                putExtra(KEY_LINEAR_INCREMENT, INCREMENT)
                manager.sendBroadcast(this)
            }

        currentTime = currentTime.plus(INCREMENT)
    }

    private fun startTimerRunnable() {
        timer = Timer()
            .apply {
                schedule(
                    TimerRunnable(this@TimerService),
                    INCREMENT.toLong(),
                    INCREMENT.toLong()
                )
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    companion object {
        // define the period of time for the UI updates here
        const val INCREMENT = SECOND_IN_MILLIS * 1
        const val DEFAULT_START_TIME = 0
    }
}
