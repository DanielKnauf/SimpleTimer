package knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import knaufdan.android.arch.databinding.bindTo
import knaufdan.android.core.util.safeValue

class NumberPickersViewModel(
    selectedValue: MediatorLiveData<Int>,
    config: NumberPickerConfig
) : INumberPickersViewModel {
    override val firstNumber: MutableLiveData<Int> = MutableLiveData(config.firstValueInitiation)
    override val secondNumber: MutableLiveData<Int> = MutableLiveData(config.secondValueInitiation)
    override val thirdNumber: MutableLiveData<Int> = MutableLiveData(config.thirdValueInitiation)

    init {
        selectedValue.bindTo(
            firstSource = firstNumber,
            secondSource = secondNumber,
            thirdSource = thirdNumber
        ) { firstNumber, secondNumber, thirdNumber ->
            val first = firstNumber.safeValue().times(config.firstValueModification)
            val second = secondNumber.safeValue().times(config.secondValueModification)
            val third = thirdNumber.safeValue().times(config.thirdValueModification)
            first + second + third
        }
    }
}
