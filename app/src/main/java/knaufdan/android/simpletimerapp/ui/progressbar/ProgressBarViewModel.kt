package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MutableLiveData

interface ProgressBarViewModel {

    val progress: MutableLiveData<Int>
    val maximum: MutableLiveData<Int>
    val isPaused: MutableLiveData<Boolean>

    fun increaseProgress(increment: Int)

    fun calculateRemainingProgress(): Long
}