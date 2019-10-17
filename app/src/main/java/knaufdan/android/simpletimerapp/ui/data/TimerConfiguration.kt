package knaufdan.android.simpletimerapp.ui.data

data class TimerConfiguration(
    val timePerCycle: Int,
    val timeUnitName: String,
    val isOnRepeat: Boolean
) {

    val timeUnit
        get() = this.timeUnitName.parseToTimeUnit()
}
