package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import java.util.Date
import javax.inject.Inject
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.alarm.AlarmService
import knaufdan.android.core.audio.AudioService
import knaufdan.android.core.service.ServiceUtil
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.ui.progressbar.ProgressBarViewModel
import knaufdan.android.simpletimerapp.ui.progressbar.TimerProgressViewModel
import knaufdan.android.simpletimerapp.util.Constants.KEY_ADJUSTED_PROGRESS
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_LINEAR_INCREMENT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS
import knaufdan.android.simpletimerapp.util.UnBoxUtil.safeUnBox
import knaufdan.android.simpletimerapp.util.alarm.AlarmReceiver
import knaufdan.android.simpletimerapp.util.broadcastreceiver.BroadcastUtil
import knaufdan.android.simpletimerapp.util.broadcastreceiver.UpdateReceiver
import knaufdan.android.simpletimerapp.util.service.Action
import knaufdan.android.simpletimerapp.util.service.TimerService
import knaufdan.android.simpletimerapp.util.service.TimerState
import knaufdan.android.simpletimerapp.util.service.TimerState.FINISH_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState.PAUSE_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState.RESTARTED_IN_BACKGROUND

class TimerFragmentViewModel @Inject constructor(
    private val alarmService: AlarmService,
    private val audioService: AudioService,
    private val broadcastUtil: BroadcastUtil,
    private val navigator: Navigator,
    private val serviceUtil: ServiceUtil,
    private val sharedPrefService: SharedPrefService
) : BaseViewModel(), ProgressBarViewModel by TimerProgressViewModel() {

    private var timerFinished = false
    val isPaused = MutableLiveData(false)

    private var isOnRepeat = false

    private val updateReceiver =
        UpdateReceiver(Action.values()) { action: String, extras: Bundle? ->
            perform(
                action,
                extras
            )
        }

    private fun perform(
        receivedAction: String,
        bundle: Bundle?
    ) {
        when (Action.valueOf(receivedAction)) {
            Action.INCREASE -> increase(bundle = bundle)
            Action.FINISH -> finish()
        }
    }

    private fun increase(bundle: Bundle?) {
        val increment = bundle?.getInt(KEY_LINEAR_INCREMENT, SECOND_IN_MILLIS) ?: SECOND_IN_MILLIS
        increaseProgress(increment = increment)
    }

    private fun finish() {
        audioService.play(R.raw.gong_sound)

        stopAndCheckNextAction(resetTimer = isOnRepeat)
    }

    private fun stopAndCheckNextAction(resetTimer: Boolean = false) {
        stopReceivingUpdates()

        val maxValue = maximum.value ?: 0

        if (resetTimer && maxValue > 0) {
            resetTimer(maxValue = maxValue)
        } else {
            finishAndQuit()
        }
    }

    private fun resetTimer(maxValue: Int) {
        progress.value = 0
        registerAndStart(maxValue = maxValue)
    }

    private fun finishAndQuit() {
        timerFinished = true
        releaseResources()
        navigator.backPressed()
    }

    override fun handleBundle(bundle: Bundle?) {
        super.handleBundle(bundle = bundle)

        if (hasTimerState(RESTARTED_IN_BACKGROUND)) {
            isOnRepeat = true
        } else {
            bundle?.apply {
                maximum.value = getInt(KEY_CURRENT_MAXIMUM)
                sharedPrefService.saveTo(
                    key = KEY_CURRENT_MAXIMUM,
                    value = maximum.value
                )
                broadcastUtil.registerBroadcastReceiver(broadcastReceiver = updateReceiver)
                serviceUtil.startService(
                    clazz = TimerService::class,
                    bundle = this
                )
                isOnRepeat = getBoolean(KEY_IS_ON_REPEAT, false)
            }
        }
    }

    fun View.onPauseClicked() {
        isPaused.value = if (safeUnBox(isPaused.value)) {
            val maxValue = maximum.value ?: 0
            val adjustedTime = progress.value ?: 0
            registerAndStart(
                maxValue = maxValue,
                adjustedTime = adjustedTime
            )
            false
        } else {
            stopReceivingUpdates()
            true
        }
    }

    fun View.onStopClicked() {
        stopAndCheckNextAction(resetTimer = false)
    }

    fun stopReceivingUpdates() {
        broadcastUtil.unregisterBroadcastReceiver(broadcastReceiver = updateReceiver)
        serviceUtil.stopService(clazz = TimerService::class)
    }

    fun setUpAlarm() {
        sharedPrefService.saveTo(
            key = KEY_TIMER_STATE,
            value = PAUSE_STATE
        )
        sharedPrefService.saveTo(
            key = KEY_PAUSE_TIME,
            value = Date().time
        )
        alarmService.setAlarm(
            timeToWakeFromNow = calculateRemainingProgress(),
            extras = createBundleForAlarmService(),
            broadcastReceiverType = AlarmReceiver::class.java
        )
    }

    private fun createBundleForAlarmService() = Bundle().apply {
        putBoolean(KEY_IS_ON_REPEAT, isOnRepeat)
        putInt(KEY_CURRENT_MAXIMUM, maximum.value ?: 0)
    }

    fun restart() {
        if (hasTimerState(PAUSE_STATE) || hasTimerState(RESTARTED_IN_BACKGROUND)) {
            alarmService.cancelAlarm(broadcastReceiverType = AlarmReceiver::class.java)

            if (hasTimerState(RESTARTED_IN_BACKGROUND)) {
                progress.value = 0
                maximum.value = sharedPrefService.retrieveInt(key = KEY_CURRENT_MAXIMUM)
            }

            val pauseTime = sharedPrefService.retrieveLong(key = KEY_PAUSE_TIME)
            val delta = (Date().time - pauseTime).toInt()
            increaseProgress(delta)

            val max = maximum.value ?: 0
            val current = progress.value ?: 0

            registerAndStart(
                maxValue = max,
                adjustedTime = current.plus(delta)
            )
        }
    }

    private fun registerAndStart(
        maxValue: Int,
        adjustedTime: Int = 0
    ) {
        broadcastUtil.registerBroadcastReceiver(broadcastReceiver = updateReceiver)
        serviceUtil.startService(
            TimerService::class,
            createBundleForTimerService(
                max = maxValue,
                adjustedTime = adjustedTime
            )
        )
    }

    private fun createBundleForTimerService(
        max: Int,
        adjustedTime: Int = 0
    ) = Bundle().apply {
        putInt(KEY_CURRENT_MAXIMUM, max)
        putInt(KEY_ADJUSTED_PROGRESS, adjustedTime)
    }

    fun isFinished() = timerFinished || hasTimerState(FINISH_STATE)

    private fun hasTimerState(expectedState: TimerState) =
        sharedPrefService.retrieveString(KEY_TIMER_STATE) == expectedState.name

    fun releaseResources() {
        audioService.release(R.raw.gong_sound)
        stopReceivingUpdates()
    }
}
