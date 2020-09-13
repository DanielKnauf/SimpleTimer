package knaufdan.android.simpletimerapp.ui.fragments.timer

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import knaufdan.android.arch.mvvm.implementation.AndroidBaseViewModel
import knaufdan.android.arch.navigation.INavigationService
import knaufdan.android.core.alarm.IAlarmService
import knaufdan.android.core.preferences.ISharedPrefService
import knaufdan.android.core.util.UnBoxUtil.safeUnBox
import knaufdan.android.services.service.IServiceDispatcher
import knaufdan.android.services.service.broadcast.Action
import knaufdan.android.services.service.broadcast.ActionDispatcher
import knaufdan.android.services.service.broadcast.IBroadcastService
import knaufdan.android.services.userinteraction.audio.IAudioService
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.timer.progress.IProgressViewModel
import knaufdan.android.simpletimerapp.ui.fragments.timer.progress.implementation.TimerProgressViewModel
import knaufdan.android.simpletimerapp.util.Constants.KEY_ADJUSTED_PROGRESS
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_LINEAR_INCREMENT
import knaufdan.android.simpletimerapp.util.Constants.KEY_PAUSE_TIME
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS
import knaufdan.android.simpletimerapp.util.alarm.AlarmReceiver
import knaufdan.android.simpletimerapp.util.service.TimerAction
import knaufdan.android.simpletimerapp.util.service.TimerService
import knaufdan.android.simpletimerapp.util.service.TimerState
import java.util.Date
import javax.inject.Inject

class TimerFragmentViewModel @Inject constructor(
    private val alarmService: IAlarmService,
    private val audioService: IAudioService,
    private val broadcastService: IBroadcastService,
    private val navigationService: INavigationService,
    private val serviceDispatcher: IServiceDispatcher,
    private val sharedPrefService: ISharedPrefService
) : AndroidBaseViewModel(), IProgressViewModel by TimerProgressViewModel() {
    val isPaused = MutableLiveData(false)

    private var timerFinished = false
    private var isOnRepeat = false

    private val actionDispatcher: ActionDispatcher<TimerAction> by lazy {
        ActionDispatcher(TimerAction.values()) { action: Action, extras: Bundle? ->
            action.perform(
                extras
            )
        }
    }

    private fun Action.perform(bundle: Bundle?) =
        when (TimerAction.valueOf(this)) {
            TimerAction.INCREASE -> increase(bundle = bundle)
            TimerAction.FINISH -> finish()
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
        navigationService.onBackPressed()
    }

    override fun onFirstStart(bundle: Bundle?) {
        super.onFirstStart(bundle)

        if (hasTimerState(TimerState.RESTARTED_IN_BACKGROUND)) {
            isOnRepeat = true
        } else {
            bundle?.apply {
                val newMax = getInt(KEY_CURRENT_MAXIMUM)
                maximum.value = newMax
                sharedPrefService.put(
                    key = KEY_CURRENT_MAXIMUM,
                    value = newMax
                )
                isOnRepeat = getBoolean(KEY_IS_ON_REPEAT, false)
                resetTimer(newMax)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        if (safeUnBox(isPaused.value)) {
            return
        }

        restart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPaused() {
        stopReceivingUpdates()

        if (doNotSetUpAlarm()) {
            return
        }

        setUpAlarm()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopped() {
        releaseResources()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        releaseResources()
    }

    private fun doNotSetUpAlarm() =
        isBackPressed ||
            isFinished() ||
            safeUnBox(isPaused.value)

    fun onPauseClicked() {
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

    fun onStopClicked() {
        stopAndCheckNextAction(false)
    }

    private fun stopReceivingUpdates() {
        broadcastService.unregisterLocalBroadcastReceiver(actionDispatcher)
        serviceDispatcher.stopService(TimerService::class)
    }

    private fun setUpAlarm() {
        sharedPrefService.put(
            key = KEY_TIMER_STATE,
            value = TimerState.PAUSE_STATE
        )
        sharedPrefService.put(
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

    private fun restart() {
        if (hasTimerState(TimerState.PAUSE_STATE) || hasTimerState(TimerState.RESTARTED_IN_BACKGROUND)) {
            alarmService.cancelAlarm(AlarmReceiver::class.java)

            if (hasTimerState(TimerState.RESTARTED_IN_BACKGROUND)) {
                progress.value = 0
                maximum.value = sharedPrefService.getInt(KEY_CURRENT_MAXIMUM)
            }

            val pauseTime = sharedPrefService.getLong(KEY_PAUSE_TIME)
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
        broadcastService.registerLocalBroadcastReceiver(actionBroadcastReceiver = actionDispatcher)
        serviceDispatcher.startService(
            serviceClass = TimerService::class,
            bundle = createBundleForTimerService(
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

    private fun isFinished() = timerFinished || hasTimerState(TimerState.FINISH_STATE)

    private fun hasTimerState(expectedState: TimerState) =
        sharedPrefService.getString(KEY_TIMER_STATE) == expectedState.name

    private fun releaseResources() {
        audioService.release(R.raw.gong_sound)
        stopReceivingUpdates()
    }
}
