package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.ui.progressbar.ProgressBarViewModel
import knaufdan.android.simpletimerapp.ui.progressbar.TimerProgressViewModel
import knaufdan.android.simpletimerapp.util.Constants.KEY_ADJUSTED_PROGRESS
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_END_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_LINEAR_INCREMENT
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.SECOND
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.alarm.AlarmService
import knaufdan.android.simpletimerapp.util.broadcastreceiver.BroadcastUtil
import knaufdan.android.simpletimerapp.util.broadcastreceiver.UpdateReceiver
import knaufdan.android.simpletimerapp.util.service.Action
import knaufdan.android.simpletimerapp.util.service.ServiceUtil
import knaufdan.android.simpletimerapp.util.service.TimerService
import knaufdan.android.simpletimerapp.util.service.TimerState
import knaufdan.android.simpletimerapp.util.service.TimerState.*
import java.util.Date
import javax.inject.Inject

class TimerFragmentViewModel @Inject constructor(
    private val alarmService: AlarmService,
    private val broadcastUtil: BroadcastUtil,
    private val navigator: Navigator,
    private val serviceUtil: ServiceUtil,
    private val sharedPrefService: SharedPrefService
) : BaseViewModel(), ProgressBarViewModel by TimerProgressViewModel() {

    var timerFinished = false

    private var isOnRepeat = false

    private val updateReceiver =
        UpdateReceiver(Action.values()) { action: String, extras: Bundle? -> perform(action, extras) }

    private fun perform(receivedAction: String, bundle: Bundle?) {
        when (Action.valueOf(receivedAction)) {
            Action.INCREASE -> increase(bundle)
            Action.FINISH -> finish()
        }
    }

    private fun increase(bundle: Bundle?) {
        val increment = bundle?.getInt(KEY_LINEAR_INCREMENT, SECOND) ?: SECOND
        increaseProgress(increment)
    }

    private fun finish() {
        stopReceivingUpdates()

        val maxValue = maximum.value

        if (isOnRepeat && maxValue != null) {
            progress.value = 0
            broadcastUtil.registerBroadcastReceiver(updateReceiver)
            serviceUtil.startService(TimerService::class, createBundleForTimerService(maxValue))
        } else {
            timerFinished = true
            navigator.navigateToInput()
        }
    }

    override fun init(bundle: Bundle?) {
        super.init(bundle)

        if (hasTimerState(RESTARTED_IN_BACKGROUND)) {
            isOnRepeat = true
        } else {
            bundle?.let {
                maximum.value = it.getInt(KEY_CURRENT_END_TIME)
                sharedPrefService.saveTo(KEY_CURRENT_END_TIME, maximum.value)
                broadcastUtil.registerBroadcastReceiver(updateReceiver)
                serviceUtil.startService(TimerService::class, it)
                isOnRepeat = it.getBoolean(KEY_IS_ON_REPEAT, false)
            }
        }
    }

    fun stopReceivingUpdates() {
        broadcastUtil.unregisterBroadcastReceiver(updateReceiver)
        serviceUtil.stopService(TimerService::class)
    }

    fun setUpAlarm() {
        sharedPrefService.saveTo(KEY_TIMER_STATE, PAUSE_STATE)
        sharedPrefService.saveTo(KEY_PAUSE_TIME, Date().time)
        alarmService.setAlarm(calculateRemainingProgress(), createBundleForAlarmService())
    }

    private fun createBundleForAlarmService() = Bundle().apply {
        putBoolean(KEY_IS_ON_REPEAT, isOnRepeat)
        putInt(KEY_CURRENT_END_TIME, maximum.value ?: 0)
    }

    fun restart() {
        if (hasTimerState(PAUSE_STATE) || hasTimerState(RESTARTED_IN_BACKGROUND)) {
            alarmService.cancelAlarm()

            if (hasTimerState(RESTARTED_IN_BACKGROUND)) {
                progress.value = 0
                maximum.value = sharedPrefService.retrieveInt(KEY_CURRENT_END_TIME)
            }

            val pauseTime = sharedPrefService.retrieveLong(KEY_PAUSE_TIME)
            val delta = (Date().time - pauseTime).toInt()
            increaseProgress(delta)

            val max = maximum.value ?: 0
            val current = progress.value ?: 0

            broadcastUtil.registerBroadcastReceiver(updateReceiver)
            serviceUtil.startService(TimerService::class, createBundleForTimerService(max, current.plus(delta)))
        }
    }

    private fun createBundleForTimerService(max: Int, adjustedTime: Int = 0) = Bundle().apply {
        putInt(KEY_CURRENT_END_TIME, max)
        putInt(KEY_ADJUSTED_PROGRESS, adjustedTime)
    }

    fun isFinished() = timerFinished || hasTimerState(FINISH_STATE)

    private fun hasTimerState(expectedState: TimerState) =
        sharedPrefService.retrieveString(KEY_TIMER_STATE) == expectedState.name
}