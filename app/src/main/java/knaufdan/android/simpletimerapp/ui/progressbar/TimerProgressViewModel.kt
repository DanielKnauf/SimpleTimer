package knaufdan.android.simpletimerapp.ui.progressbar

import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum = ExtMutableLiveData(0)
    override val progress = ExtMutableLiveData(0)
    override val isPaused = ExtMutableLiveData(false)

    override fun increaseProgress(increment: Int) =
        progress.postValue(progress.value?.plus(increment))

    override fun calculateRemainingProgress() =
        progress.value?.run { maximum.value?.minus(this) }?.toLong() ?: 0
}
