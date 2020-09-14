package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector

import androidx.lifecycle.MediatorLiveData

interface ITimeSelectorComponentFactory {
    fun create(
        selectedTime: MediatorLiveData<Int>,
        config: TimeSelectorConfig
    ): ITimeSelectorComponent
}
