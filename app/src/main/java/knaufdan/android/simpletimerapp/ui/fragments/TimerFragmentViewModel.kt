package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.ui.progressbar.ProgressBarViewModel
import knaufdan.android.simpletimerapp.ui.progressbar.TimerProgressViewModel
import knaufdan.android.simpletimerapp.util.Constants.END_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.INCREMENT_KEY
import knaufdan.android.simpletimerapp.util.Constants.SECOND
import knaufdan.android.simpletimerapp.util.alarm.AlarmService
import knaufdan.android.simpletimerapp.util.broadcastreceiver.BroadcastUtil
import knaufdan.android.simpletimerapp.util.broadcastreceiver.UpdateReceiver
import knaufdan.android.simpletimerapp.util.service.Action
import knaufdan.android.simpletimerapp.util.service.ServiceUtil
import knaufdan.android.simpletimerapp.util.service.TimerService
import javax.inject.Inject

class TimerFragmentViewModel @Inject constructor(
    private val alarmService: AlarmService,
    private val broadcastUtil: BroadcastUtil,
    private val navigator: Navigator,
    private val serviceUtil: ServiceUtil
) : BaseViewModel(), ProgressBarViewModel by TimerProgressViewModel() {

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
        navigator.navigateToInput()
    }

    override fun init(bundle: Bundle?) {
        super.init(bundle)

        bundle?.let {
            maximum.value = it.getInt(END_TIME_KEY)
            serviceUtil.startService(TimerService::class, it)
            broadcastUtil.registerBroadcastReceiver(updateReceiver)
        }
    }

    fun stopReceivingUpdates() {
        broadcastUtil.unregisterBroadcastReceiver(updateReceiver)
        serviceUtil.stopService(TimerService::class)
    }

    fun setUpAlarm() {
        alarmService.setAlarm(calculateRemainingProgress())
    }
}