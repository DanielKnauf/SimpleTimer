package knaufdan.android.simpletimerapp.util.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

//TODO rework timer for the purpose of this app∆
class TimerService @Inject constructor() : Service() {

    private val random = Random()
    private var handler: Handler? = null
    private var randomTimerRunnable: TimerRunnable? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        handler = Handler()
        startHandlerWithRandomTimer()
        return Service.START_STICKY
    }

    fun startHandlerWithRandomTimer() {
        handler?.let {
            val timeInMillis = generateTimeUntilNextPhotoNotification()
            randomTimerRunnable = TimerRunnable(this, timeInMillis / 1000)
            it.postDelayed(randomTimerRunnable, timeInMillis.toLong())
        }
    }

    private fun generateTimeUntilNextPhotoNotification(): Int {
        return (BASE_WAITING_TIME + random.nextInt(MAX_RANGE_OF_ADDED_TIME)) * MODIFIER_FOR_SECONDS
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(randomTimerRunnable)
        handler = null
    }

    companion object {

        //all times are in milliseconds
        private val BASE_WAITING_TIME = 1
        private val MAX_RANGE_OF_ADDED_TIME = 10
        private val MODIFIER_FOR_SECONDS = 1000
    }
}


