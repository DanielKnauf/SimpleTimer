package knaufdan.android.simpletimerapp.util

/**
 * Based on a time in millis, the composition of hours, minutes and seconds is determined.
 *
 * @return <hours, minutes, seconds>
 */
fun Int.determineTimeSections(): Triple<Int, Int, Int> = run {
    val hours = (this / Constants.HOUR_IN_MILLIS)
    val minutes = (this % Constants.HOUR_IN_MILLIS / Constants.MINUTE_IN_MILLIS)
    val seconds = (this % Constants.MINUTE_IN_MILLIS / Constants.SECOND_IN_MILLIS)
    Triple(hours, minutes, seconds)
}
