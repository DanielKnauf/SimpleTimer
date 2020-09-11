package knaufdan.android.simpletimerapp.ui.fragments.timer.progress

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

interface IProgressViewModel {
    val progress: MutableLiveData<Int>
    val inverseProgress: MediatorLiveData<Int>
    val maximum: MutableLiveData<Int>

    fun increaseProgress(increment: Int)

    fun calculateRemainingProgress(): Long
}
