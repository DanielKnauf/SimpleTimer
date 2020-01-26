package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import javax.inject.Inject
import knaufdan.android.arch.databinding.bindTo
import knaufdan.android.arch.mvvm.implementation.BaseViewModel
import knaufdan.android.arch.navigation.INavigationService
import knaufdan.android.core.preferences.ISharedPrefService
import knaufdan.android.core.util.UnBoxUtil.safeUnBox
import knaufdan.android.core.util.safeValue
import knaufdan.android.simpletimerapp.ui.data.TimerConfiguration
import knaufdan.android.simpletimerapp.util.Constants.HOUR_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_CONFIGURATION
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.Constants.MINUTE_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS
import knaufdan.android.simpletimerapp.util.determineClockSections
import knaufdan.android.simpletimerapp.util.service.TimerState

class InputFragmentViewModel @Inject constructor(
    private val navigationService: INavigationService,
    private val sharedPrefService: ISharedPrefService
) : BaseViewModel() {
    val isEnabled = MediatorLiveData<Boolean>()
    val isOnRepeat = MutableLiveData(false)
    val hours = MutableLiveData(0)
    val minutes = MutableLiveData(1)
    val seconds = MutableLiveData(0)
    private val timePerCycle = MediatorLiveData<Int>()

    fun onStartClicked() {
        timePerCycle.value?.apply {
            val isOnRepeat = safeUnBox(isOnRepeat.value)

            TimerConfiguration(
                timePerCycle = this,
                isOnRepeat = isOnRepeat
            ).apply {
                sharedPrefService.saveAsJsonTo(
                    key = KEY_TIMER_CONFIGURATION,
                    value = this
                )

                navigationService.goToFragment(
                    fragment = TimerFragment().apply {
                        arguments = Bundle().apply {
                            putInt(KEY_CURRENT_MAXIMUM, timePerCycle)
                            putBoolean(KEY_IS_ON_REPEAT, isOnRepeat)
                        }
                    },
                    addToBackStack = true
                )
            }
        }
    }

    init {
        resetState()

        timePerCycle.bindTo(
            firstSource = hours,
            secondSource = minutes,
            thirdSource = seconds
        ) { h, m, s ->
            val hours = h.safeValue().times(HOUR_IN_MILLIS)
            val minutes = m.safeValue().times(MINUTE_IN_MILLIS)
            val seconds = s.safeValue().times(SECOND_IN_MILLIS)
            hours + minutes + seconds
        }

        isEnabled.bindTo(source = timePerCycle) { time -> time > 0 }

        sharedPrefService.retrieveJson(
            KEY_TIMER_CONFIGURATION,
            TimerConfiguration::class
        )?.apply {
            this@InputFragmentViewModel.timePerCycle.value = this.timePerCycle
            timePerCycle.determineClockSections().apply {
                hours.value = first
                minutes.value = second
                seconds.value = third
            }
            this@InputFragmentViewModel.isOnRepeat.value = this.isOnRepeat
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        resetState()
    }

    private fun resetState() = sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESET_STATE)
}
