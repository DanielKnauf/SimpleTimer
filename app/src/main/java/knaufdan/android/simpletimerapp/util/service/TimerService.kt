package knaufdan.android.simpletimerapp.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.android.AndroidInjection
import knaufdan.android.simpletimerapp.util.Constants.END_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.INCREMENT_KEY
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.SECOND
import java.util.*
import javax.inject.Inject

class TimerService @Inject constructor() : Service() {

    private lateinit var manager: LocalBroadcastManager
    private var timer: Timer? = null
    private var timerTask: TimerTask? = TimerRunnable(this)

    private var endTime: Int = -1
    private var currentTime = 0

    //define the period of time for the UI updates here
    private val increment = SECOND * 1

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        manager = LocalBroadcastManager.getInstance(this.applicationContext)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        endTime = intent.getIntExtra(END_TIME_KEY, MINUTE)
        currentTime = 0
        startTimerRunnable()
        return Service.START_STICKY
    }

    fun sendUpdate() {
        val intent = Intent()
            .apply { action = if (endTime <= currentTime) Action.FINISH.name else Action.INCREASE.name }
            .apply { putExtra(INCREMENT_KEY, increment) }

        currentTime += increment
        manager.sendBroadcast(intent)
    }

    private fun startTimerRunnable() {
        timer = Timer()
            .apply {
                schedule(timerTask, increment.toLong(), increment.toLong())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }
}


