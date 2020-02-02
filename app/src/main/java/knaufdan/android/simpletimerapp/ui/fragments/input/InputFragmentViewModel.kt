package knaufdan.android.simpletimerapp.ui.fragments.input

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import knaufdan.android.arch.databinding.livedata.bindTo
import knaufdan.android.arch.mvvm.implementation.BaseViewModel
import knaufdan.android.arch.navigation.INavigationService
import knaufdan.android.core.preferences.ISharedPrefService
import knaufdan.android.core.util.UnBoxUtil.safeUnBox
import knaufdan.android.simpletimerapp.ui.data.TimerConfiguration
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.TimeSelector
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.TimeSelectorConfig
import knaufdan.android.simpletimerapp.ui.fragments.timer.TimerFragment
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_CONFIGURATION
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.determineClockSections
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(
    private val navigationService: INavigationService,
    private val sharedPrefService: ISharedPrefService
) : BaseViewModel() {
    private val timePerCycle = MediatorLiveData<Int>()
    val isEnabled = MediatorLiveData<Boolean>()
    val isOnRepeat = MutableLiveData(false)
    val timeSelector: TimeSelector

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

        isEnabled.bindTo(source = timePerCycle) { time -> time > 0 }

        val timeSelectorConfig =
            sharedPrefService.retrieveJson(
                KEY_TIMER_CONFIGURATION,
                TimerConfiguration::class
            )?.run {
                this@InputFragmentViewModel.isOnRepeat.value = this.isOnRepeat
                extractTimeSelectorConfig()
            } ?: TimeSelectorConfig.DEFAULT

        timeSelector = TimeSelector(
            selectedTime = timePerCycle,
            config = timeSelectorConfig
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        resetState()
    }

    private fun TimerConfiguration.extractTimeSelectorConfig() =
        timePerCycle.determineClockSections().run {
            TimeSelectorConfig(
                hours = first,
                minutes = second,
                seconds = third
            )
        }

    private fun resetState() = sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESET_STATE)
}
