package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.databinding.ExtMutableLiveData
import knaufdan.android.simpletimerapp.util.bindTo

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum = ExtMutableLiveData(0)
    override val progress = ExtMutableLiveData(0)
    override val inverseProgress = MediatorLiveData<Int>()

    init {
        inverseProgress.bindTo(
            source = progress,
            bindSafe = false
        ) { progress ->
            maximum.value?.minus(progress) ?: 0
        }
    }

    override fun increaseProgress(increment: Int) =
        progress.postValue(progress.value?.plus(increment))

    override fun calculateRemainingProgress() =
        progress.value?.run { maximum.value?.minus(this) }?.toLong() ?: 0
}
