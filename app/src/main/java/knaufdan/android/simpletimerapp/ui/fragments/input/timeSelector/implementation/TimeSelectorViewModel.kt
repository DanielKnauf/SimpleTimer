package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.implementation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.arch.base.component.IComponentViewModel
import knaufdan.android.arch.databinding.livedata.bindTo
import knaufdan.android.arch.databinding.views.Suffixes
import knaufdan.android.core.util.safeValue
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.TimeSelectorConfig
import knaufdan.android.simpletimerapp.util.Constants

class TimeSelectorViewModel(
    selectedValue: MediatorLiveData<Int>,
    config: TimeSelectorConfig
) : IComponentViewModel {
    val hours: MutableLiveData<Int> = MutableLiveData(config.hours)
    val hourSuffixes = Suffixes(
        singular = R.string.time_selector_hour_suffix_singular,
        plural = R.string.time_selector_hour_suffix_plural
    )

    val minutes: MutableLiveData<Int> = MutableLiveData(config.minutes)
    val minuteSuffixes = Suffixes(
        singular = R.string.time_selector_minute_suffix_singular,
        plural = R.string.time_selector_minute_suffix_plural
    )

    val seconds: MutableLiveData<Int> = MutableLiveData(config.seconds)
    val secondSuffixes = Suffixes(
        singular = R.string.time_selector_second_suffix_singular,
        plural = R.string.time_selector_second_suffix_plural
    )

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
