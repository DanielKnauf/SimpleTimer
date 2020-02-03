package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.arch.databinding.livedata.bindTo
import knaufdan.android.core.util.safeValue
import knaufdan.android.simpletimerapp.util.Constants

class TimeSelectorViewModel(
    selectedValue: MediatorLiveData<Int>,
    config: TimeSelectorConfig
) : ITimeSelectorViewModel {
    override val hours: MutableLiveData<Int> = MutableLiveData(config.hours)
    override val minutes: MutableLiveData<Int> = MutableLiveData(config.minutes)
    override val seconds: MutableLiveData<Int> = MutableLiveData(config.seconds)

    init {
        selectedValue.bindTo(
            firstSource = hours,
            secondSource = minutes,
            thirdSource = seconds
        ) { h, m, s ->
            val hours = h.safeValue().times(Constants.HOUR_IN_MILLIS)
            val minutes = m.safeValue().times(Constants.MINUTE_IN_MILLIS)
            val seconds = s.safeValue().times(Constants.SECOND_IN_MILLIS)
            hours + minutes + seconds
        }
    }
}
