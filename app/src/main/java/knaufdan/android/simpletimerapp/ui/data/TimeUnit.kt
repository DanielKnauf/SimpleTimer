package knaufdan.android.simpletimerapp.ui.data

import knaufdan.android.simpletimerapp.util.Constants

enum class TimeUnit(
    val displayName: String,
    val timeInMilliSeconds: Int
) {
    MINUTE("Minute", Constants.MINUTE_IN_MILLIS),
    SECOND("Second", Constants.SECOND_IN_MILLIS)
}

fun String.parseToTimeUnit() = TimeUnit.values().firstOrNull { timeUnit ->
    timeUnit.name == this
} ?: TimeUnit.MINUTE

fun Int.parseToTimeUnit() =
    if (this < TimeUnit.values().size) TimeUnit.values()[this]
    else TimeUnit.MINUTE
