package knaufdan.android.simpletimerapp.util.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmService @Inject constructor(private val contextProvider: ContextProvider) {

    /**
     * Sets up an alarm which is triggered after a period of time.
     *
     * @param timeToWakeFromNow - period of time greater than zero after which [AlarmReceiver] is triggered.
     */
    fun setAlarm(timeToWakeFromNow: Long) {
        if (timeToWakeFromNow <= 0) {
            return
        }

        val wakeUpTime = System.currentTimeMillis().plus(timeToWakeFromNow)
        val (alarmManager, pendingIntent) = contextProvider.context.buildTools()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
    }

    /**
     * Stops the current alarm.
     */
    fun cancelAlarm() {
        val (alarmManager, pendingIntent) = contextProvider.context.buildTools()
        alarmManager.cancel(pendingIntent)
    }

    private fun Context.buildTools() = this.getAlarmManager() to this.buildPendingIntent()

    private fun Context.getAlarmManager() = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun Context.buildPendingIntent() = PendingIntent.getBroadcast(
        this,
        0,
        Intent(this, AlarmReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}