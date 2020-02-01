package knaufdan.android.simpletimerapp.ui.fragments.input

import androidx.lifecycle.MediatorLiveData
import knaufdan.android.arch.databinding.BindingKey
import knaufdan.android.arch.databinding.IComponent
import knaufdan.android.arch.databinding.LayoutRes
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker.INumberPickersViewModel
import knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker.NumberPickerConfig
import knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker.NumberPickersViewModel

class TimeSelector(
    private val selectedTime: MediatorLiveData<Int>,
    private val config: NumberPickerConfig
) : IComponent<INumberPickersViewModel> {
    private val viewModel: INumberPickersViewModel by lazy {
        NumberPickersViewModel(
            selectedTime,
            config
        )
    }

    override fun getBindingKey(): BindingKey = BR.viewModel

    override fun getDataSource(): INumberPickersViewModel = viewModel

    override fun getLayoutRes(): LayoutRes = R.layout.time_selector
}
