package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MutableLiveData

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum: MutableLiveData<Int> = MutableLiveData()
    override val progress: MutableLiveData<Int> = MutableLiveData()

    init {
        progress.value = 0
        maximum.value = 0
    }

    override fun increaseProgress(increment: Int) {
        progress.postValue(progress.value?.plus(increment))
    }

    override fun calculateRemainingProgress(): Long {
        return progress.value?.let { maximum.value?.minus(it) }?.toLong() ?: 0
    }
}
