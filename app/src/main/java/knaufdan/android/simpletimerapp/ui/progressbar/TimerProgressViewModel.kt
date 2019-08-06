package knaufdan.android.simpletimerapp.ui.progressbar

import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum = ExtMutableLiveData(0)
    override val progress = ExtMutableLiveData(0)

    override fun increaseProgress(increment: Int) = progress.postValue(progress.value?.plus(increment))

    override fun calculateRemainingProgress() = progress.value?.let { maximum.value?.minus(it) }?.toLong() ?: 0
}
