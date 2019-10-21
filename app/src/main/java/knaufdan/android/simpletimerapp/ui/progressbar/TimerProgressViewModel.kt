package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.core.databinding.bindTo

class TimerProgressViewModel : ProgressBarViewModel {
    override val maximum = MutableLiveData(0)
    override val progress = MutableLiveData(0)
    override val inverseProgress = MediatorLiveData<Int>()

    init {
        inverseProgress.bindTo(
            source = progress,
            postOnlyDifferentValues = false
        ) { progress ->
            maximum.value?.minus(progress) ?: 0
        }
    }

    override fun increaseProgress(increment: Int) =
        progress.postValue(progress.value?.plus(increment))

    override fun calculateRemainingProgress() =
        progress.value?.run { maximum.value?.minus(this) }?.toLong() ?: 0
}
