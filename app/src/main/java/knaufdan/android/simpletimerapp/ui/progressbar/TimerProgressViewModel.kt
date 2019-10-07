package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum = ExtMutableLiveData(0)
    override val progress = ExtMutableLiveData(0)
    override val inverseProgress = MediatorLiveData<Int>()

    init {
        inverseProgress.addSource(progress) { progress ->
            inverseProgress.postValue(maximum.value?.minus(progress) ?: 0)
        }
    }

    override fun increaseProgress(increment: Int) =
        progress.postValue(progress.value?.plus(increment))

    override fun calculateRemainingProgress() =
        progress.value?.run { maximum.value?.minus(this) }?.toLong() ?: 0
}
