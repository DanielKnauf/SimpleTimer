package knaufdan.android.simpletimerapp.ui.fragments.timer.progress.implementation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.arch.databinding.livedata.bindTo
import knaufdan.android.simpletimerapp.ui.fragments.timer.progress.IProgressViewModel

class TimerProgressViewModel : IProgressViewModel {
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
