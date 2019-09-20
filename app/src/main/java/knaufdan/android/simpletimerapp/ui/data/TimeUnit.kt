package knaufdan.android.simpletimerapp.ui.data

import knaufdan.android.simpletimerapp.util.Constants

enum class TimeUnit(
    val displayText: String,
    val timeInMilliSeconds: Int
) {
    MINUTE(
        displayText = "minutes",
        timeInMilliSeconds = Constants.MINUTE_IN_MILLIS
    ),
    SECOND(
        displayText = "seconds",
        timeInMilliSeconds = Constants.SECOND_IN_MILLIS
    ),
    HOUR(
        displayText = "hours",
        timeInMilliSeconds = Constants.HOUR_IN_MILLIS
    )
}

fun String.parseToTimeUnit() = TimeUnit.values().firstOrNull { timeUnit ->
    timeUnit.name == this
} ?: TimeUnit.MINUTE

fun Int.parseToTimeUnit() =
    if (this < TimeUnit.values().size) TimeUnit.values()[this]
    else TimeUnit.MINUTE
