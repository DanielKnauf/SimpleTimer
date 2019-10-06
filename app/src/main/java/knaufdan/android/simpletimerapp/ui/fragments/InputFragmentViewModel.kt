package knaufdan.android.simpletimerapp.ui.fragments

import androidx.lifecycle.MediatorLiveData
import com.google.android.material.tabs.TabLayout
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData
import knaufdan.android.simpletimerapp.ui.data.TimeUnit
import knaufdan.android.simpletimerapp.ui.data.TimerConfiguration
import knaufdan.android.simpletimerapp.ui.data.parseToTimeUnit
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_CONFIGURATION
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.TextProvider
import knaufdan.android.simpletimerapp.util.UnBoxUtil.safeUnBox
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(
    private val navigator: Navigator,
    private val sharedPrefService: SharedPrefService,
    private val textProvider: TextProvider
) : BaseViewModel() {
    val instructionText = MediatorLiveData<String>()
    val hintText = MediatorLiveData<String>()
    val timePerCycle = ExtMutableLiveData<Int?>(1)
    val isEnabled = MediatorLiveData<Boolean>()
    val isOnRepeat = ExtMutableLiveData(false)
    val currentSelection = MediatorLiveData<Int>()
    val timeUnitSelectionItems by lazy {
        TimeUnit.values().map { timeUnit -> timeUnit.displayText }.toList()
    }
    private val timeUnitSelection = ExtMutableLiveData(TimeUnit.MINUTE.ordinal)

    fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.apply {
            timeUnitSelection.value = this.position
        }
    }

    fun onStartClicked() {
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

        connect(source = timeUnitSelection, target = currentSelection)

        connect(source = timeUnitSelection, target = instructionText) { s ->
            textProvider.getText(
                R.string.timer_instruction,
                s.parseToTimeUnit().displayText
            )
        }

        connect(source = timeUnitSelection, target = hintText) { s ->
            s.parseToTimeUnit().displayText
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