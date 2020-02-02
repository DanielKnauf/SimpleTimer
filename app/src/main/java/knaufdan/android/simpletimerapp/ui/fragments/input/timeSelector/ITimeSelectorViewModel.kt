package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector

import androidx.lifecycle.MutableLiveData

interface ITimeSelectorViewModel {
    val hours: MutableLiveData<Int>
    val minutes: MutableLiveData<Int>
    val seconds: MutableLiveData<Int>
}
