package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.implementation

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.arch.base.component.BindingKey
import knaufdan.android.arch.base.component.LayoutRes
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.ITimeSelectorComponent
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.TimeSelectorConfig

internal class TimeSelectorComponent(
    private val selectedTime: MediatorLiveData<Int>,
    private val config: TimeSelectorConfig
) : ITimeSelectorComponent {
    private val viewModel: TimeSelectorViewModel by lazy {
        TimeSelectorViewModel(
            selectedValue = selectedTime,
            config = config
        )
    }

    override fun getBindingKey(): BindingKey = BR.viewModel

    override fun getDataSource(): TimeSelectorViewModel = viewModel

    override fun getLayoutRes(): LayoutRes = R.layout.time_selector
}
