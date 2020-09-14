package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.implementation

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.ITimeSelectorComponent
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.ITimeSelectorComponentFactory
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.TimeSelectorConfig

class TimeSelectorComponentFactory : ITimeSelectorComponentFactory {
    override fun create(
        selectedTime: MediatorLiveData<Int>,
        config: TimeSelectorConfig
    ): ITimeSelectorComponent =
        TimeSelectorComponent(
            selectedTime = selectedTime,
            config = config
        )
}
