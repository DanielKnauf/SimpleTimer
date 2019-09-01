package knaufdan.android.simpletimerapp.ui.fragments

import android.view.View
import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData
import knaufdan.android.simpletimerapp.ui.data.TimeUnit
import knaufdan.android.simpletimerapp.ui.data.TimerConfiguration
import knaufdan.android.simpletimerapp.ui.data.parseToTimeUnit
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_CONFIGURATION
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.UnBoxUtil.safeUnBox
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(
    private val navigator: Navigator,
    private val sharedPrefService: SharedPrefService
) : BaseViewModel() {

    val spinnerItems by lazy {
        TimeUnit.values().map { it.displayName }.toList()
    }

    val timeUnitSelection = ExtMutableLiveData(0)

    val timePerCycle = ExtMutableLiveData<Int?>(1)

    val isEnabled = MediatorLiveData<Boolean>()

    val isOnRepeat = ExtMutableLiveData(false)

    fun View.onStartClicked() {
        timePerCycle.value?.apply {
            val timeUnit = (timeUnitSelection.value ?: 0).parseToTimeUnit()
            val isOnRepeat = safeUnBox(isOnRepeat.value)

            sharedPrefService.saveAsJsonTo(
                KEY_TIMER_CONFIGURATION,
                TimerConfiguration(
                    timePerCycle = this,
                    timeUnitName = timeUnit.name,
                    isOnRepeat = isOnRepeat
                )
            )

            navigator.navigateToTimer(
                timerMaximum = this.times(timeUnit.timeInMilliSeconds),
                isOnRepeat = isOnRepeat
            )
        }
    }

    init {
        resetState()

        isEnabled.addSource(timePerCycle) { time ->
            isEnabled.postValue(time != null && time > 0)
        }

        sharedPrefService.retrieveJson<TimerConfiguration>(KEY_TIMER_CONFIGURATION)
            ?.apply {
                this@InputFragmentViewModel.timePerCycle.value = this.timePerCycle
                this@InputFragmentViewModel.isOnRepeat.value = this.isOnRepeat
                timeUnitSelection.value = this.timeUnit.ordinal
            }
    }

    fun resetState() {
        sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESET_STATE)
    }
}