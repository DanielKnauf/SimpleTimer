package knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector

data class TimeSelectorConfig(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {

    companion object {
        val DEFAULT: TimeSelectorConfig by lazy {
            TimeSelectorConfig(
                hours = 0,
                minutes = 1,
                seconds = 0
            )
        }
    }
}
