package knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker

import androidx.lifecycle.MutableLiveData

interface INumberPickersViewModel {
    val firstNumber: MutableLiveData<Int>
    val secondNumber: MutableLiveData<Int>
    val thirdNumber: MutableLiveData<Int>
}
