package knaufdan.android.simpletimerapp.ui.fragments

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.STATE_KEY
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(
    private val navigator: Navigator,
    sharedPrefService: SharedPrefService
) : BaseViewModel() {

    val timePerCycle: MutableLiveData<Int?> = MutableLiveData()

    val isEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    val continueButtonClick: View.OnClickListener = View.OnClickListener {
        timePerCycle.value?.let { input -> navigator.navigateToTimer(input.times(MINUTE)) }
    }

    init {
        sharedPrefService.saveTo(STATE_KEY, TimerState.RESET_STATE)

        timePerCycle.value = 1

        isEnabled.addSource(timePerCycle) {
            isEnabled.postValue(it != null && it > 0)
        }
    }
}