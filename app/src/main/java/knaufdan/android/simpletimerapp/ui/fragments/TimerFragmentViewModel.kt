package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.ui.progressbar.ProgressBarViewModel
import knaufdan.android.simpletimerapp.ui.progressbar.TimerProgressViewModel
import knaufdan.android.simpletimerapp.util.Constants.ADJUSTED_PROGRESS_KEY
import knaufdan.android.simpletimerapp.util.Constants.END_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.INCREMENT_KEY
import knaufdan.android.simpletimerapp.util.Constants.IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.PAUSE_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.SECOND
import knaufdan.android.simpletimerapp.util.Constants.STATE_KEY
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.alarm.AlarmService
import knaufdan.android.simpletimerapp.util.broadcastreceiver.BroadcastUtil
import knaufdan.android.simpletimerapp.util.broadcastreceiver.UpdateReceiver
import knaufdan.android.simpletimerapp.util.service.Action
import knaufdan.android.simpletimerapp.util.service.ServiceUtil
import knaufdan.android.simpletimerapp.util.service.TimerService
import knaufdan.android.simpletimerapp.util.service.TimerState
import java.util.*
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
        val increment = bundle?.getInt(INCREMENT_KEY, SECOND) ?: SECOND
        increaseProgress(increment)
    }

    private fun finish() {
        stopReceivingUpdates()

        if (isOnRepeat) {
            progress.value = 0
            broadcastUtil.registerBroadcastReceiver(updateReceiver)
            serviceUtil.startService(TimerService::class,
                    Bundle().apply {
                        putInt(END_TIME_KEY, maximum.value!!)
                    }
            )
        } else {
            timerFinished = true
            navigator.navigateToInput()
        }
    }

    override fun init(bundle: Bundle?) {
        super.init(bundle)

        if (sharedPrefService.retrieveString(STATE_KEY) == TimerState.RESTARTED_IN_BACKGROUND.name) {
            isOnRepeat = true
            return
        }

        bundle?.let {
            maximum.value = it.getInt(END_TIME_KEY)
            sharedPrefService.saveTo(END_TIME_KEY, maximum.value)
            broadcastUtil.registerBroadcastReceiver(updateReceiver)
            serviceUtil.startService(TimerService::class, it)
            isOnRepeat = it.getBoolean(IS_ON_REPEAT, false)
        }
    }

    fun stopReceivingUpdates() {
        broadcastUtil.unregisterBroadcastReceiver(updateReceiver)
        serviceUtil.stopService(TimerService::class)
    }

    fun setUpAlarm() {
        sharedPrefService.saveTo(STATE_KEY, TimerState.PAUSE_STATE)
        sharedPrefService.saveTo(PAUSE_TIME_KEY, Date().time)
        alarmService.setAlarm(calculateRemainingProgress(),
                Bundle().apply {
                    putBoolean(IS_ON_REPEAT, isOnRepeat)
                    putInt(END_TIME_KEY, maximum.value!!)
                }
        )
    }

    fun restart() {
        val state = sharedPrefService.retrieveString(STATE_KEY)

        if (TimerState.PAUSE_STATE.name == state || TimerState.RESTARTED_IN_BACKGROUND.name == state) {
            alarmService.cancelAlarm()

            if (TimerState.RESTARTED_IN_BACKGROUND.name == state) {
                progress.value = 0
                maximum.value = sharedPrefService.retrieveInt(END_TIME_KEY)
            }

            val pauseTime = sharedPrefService.retrieveLong(PAUSE_TIME_KEY)
            val delta = (Date().time - pauseTime).toInt()
            increaseProgress(delta)

            val max = maximum.value ?: 0
            val current = progress.value ?: 0

            val bundle: Bundle = createBundle(max, current.plus(delta))

            broadcastUtil.registerBroadcastReceiver(updateReceiver)
            serviceUtil.startService(TimerService::class, bundle)
        }
    }

    private fun createBundle(max: Int, adjustedTime: Int) = Bundle().apply {
        putInt(END_TIME_KEY, max)
        putInt(ADJUSTED_PROGRESS_KEY, adjustedTime)
    }

    fun isFinished(): Boolean = timerFinished
            || TimerState.FINISH_STATE.name == sharedPrefService.retrieveString(STATE_KEY)
}