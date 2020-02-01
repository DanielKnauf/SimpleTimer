package knaufdan.android.simpletimerapp.ui.fragments.input.numberPicker

import knaufdan.android.simpletimerapp.util.Constants

data class NumberPickerConfig(
    val firstValueInitiation: Int,
    val firstValueModification: Int = Constants.HOUR_IN_MILLIS,
    val secondValueInitiation: Int,
    val secondValueModification: Int = Constants.MINUTE_IN_MILLIS,
    val thirdValueInitiation: Int,
    val thirdValueModification: Int = Constants.SECOND_IN_MILLIS
)
