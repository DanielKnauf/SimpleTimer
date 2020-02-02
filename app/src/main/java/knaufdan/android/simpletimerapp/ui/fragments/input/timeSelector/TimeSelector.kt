package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.arch.base.component.BindingKey
import knaufdan.android.arch.base.component.IComponent
import knaufdan.android.arch.base.component.LayoutRes
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R

class TimeSelector(
    private val selectedTime: MediatorLiveData<Int>,
    private val config: TimeSelectorConfig
) : IComponent<ITimeSelectorViewModel> {
    private val viewModel: ITimeSelectorViewModel by lazy {
        TimeSelectorViewModel(
            selectedTime,
            config
        )
    }

    override fun getBindingKey(): BindingKey = BR.viewModel

    override fun getDataSource(): ITimeSelectorViewModel = viewModel

    override fun getLayoutRes(): LayoutRes = R.layout.time_selector
}