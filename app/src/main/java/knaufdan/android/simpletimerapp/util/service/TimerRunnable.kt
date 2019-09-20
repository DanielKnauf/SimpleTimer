package knaufdan.android.simpletimerapp.util.service

import java.util.TimerTask

internal class TimerRunnable(private val timerService: TimerService) : TimerTask() {
    override fun run() {
        timerService.sendUpdate()
    }
}
