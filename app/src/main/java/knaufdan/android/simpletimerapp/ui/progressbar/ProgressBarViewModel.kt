package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

interface ProgressBarViewModel {
    val progress: MutableLiveData<Int>
    val inverseProgress: MediatorLiveData<Int>
    val maximum: MutableLiveData<Int>

    fun increaseProgress(increment: Int)

    fun calculateRemainingProgress(): Long
}
