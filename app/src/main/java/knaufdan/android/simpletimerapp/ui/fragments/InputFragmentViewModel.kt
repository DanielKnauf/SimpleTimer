package knaufdan.android.simpletimerapp.ui.fragments

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import javax.inject.Inject

class InputFragmentViewModel @Inject constructor(private val navigator: Navigator) : BaseViewModel() {

    val timePerCycle: MutableLiveData<Int?> = MutableLiveData()

    val isEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    val continueButtonClick: View.OnClickListener = View.OnClickListener {
        timePerCycle.value?.let { number ->
            navigator.navigateToTimer(number)
        }
    }

    init {
        timePerCycle.value = 1

        isEnabled.addSource(timePerCycle) {
            isEnabled.postValue(it != null && it > 0)
        }
    }
}