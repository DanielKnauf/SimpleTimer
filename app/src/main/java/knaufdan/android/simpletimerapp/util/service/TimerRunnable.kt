package knaufdan.android.simpletimerapp.util.service

import android.util.Log

internal class TimerRunnable(
        private val timerService: TimerService,
        private val displayTime: Int
) : Runnable {

    override fun run() {
        Log.d("Service", "Run service with $displayTime")
        timerService.startHandlerWithRandomTimer()
    }
}

