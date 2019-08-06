package knaufdan.android.simpletimerapp.ui.fragments

import android.view.View
import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.UnBoxUtil.safeUnBox
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(
    private val navigator: Navigator,
    sharedPrefService: SharedPrefService
) : BaseViewModel() {

    val timePerCycle = ExtMutableLiveData<Int?>(1)

    val isEnabled = MediatorLiveData<Boolean>()

    val isOnRepeat = ExtMutableLiveData(false)

    val continueButtonClick = View.OnClickListener {
        timePerCycle.value?.let { input -> navigator.navigateToTimer(input.times(MINUTE), safeUnBox(isOnRepeat.value)) }
    }

    init {
        sharedPrefService.saveTo(KEY_TIMER_STATE, TimerState.RESET_STATE)

        isEnabled.addSource(timePerCycle) { time ->
            isEnabled.postValue(time != null && time > 0)
        }
    }
}