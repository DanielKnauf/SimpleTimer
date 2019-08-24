package knaufdan.android.simpletimerapp.ui.fragments

import android.view.View
import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.util.Constants
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
        SpinnerOption.values().map { it.displayName }.toList()
    }

    val selection = ExtMutableLiveData(0)

    val timePerCycle = ExtMutableLiveData<Int?>(1)

    val isEnabled = MediatorLiveData<Boolean>()

    val isOnRepeat = ExtMutableLiveData(false)

    fun onStartClicked(view: View) {
        timePerCycle.value?.let { input ->
            navigator.navigateToTimer(
                input.times(SpinnerOption.values()[selection.value ?: 0].timeUnitAdjustment),
                safeUnBox(isOnRepeat.value)
            )
        }
    }

    fun resetState() {
        sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESET_STATE)
    }

    init {
        resetState()

        isEnabled.addSource(timePerCycle) { time ->
            isEnabled.postValue(time != null && time > 0)
        }
    }

    enum class SpinnerOption constructor(
        val displayName: String,
        val timeUnitAdjustment: Int
    ) {
        MINUTE("Minute", Constants.MINUTE),
        SECOND("Second", Constants.SECOND),
    }
}