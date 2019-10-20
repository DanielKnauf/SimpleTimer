package knaufdan.android.simpletimerapp.databinding

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.Constants.HOUR_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.MINUTE_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS

@BindingAdapter(value = ["progressText"])
fun TextView.setProgressText(progress: Int?) {
    text = progress?.run {
        val hours = (this / HOUR_IN_MILLIS).addZero()
        val minutes = (this % HOUR_IN_MILLIS / MINUTE_IN_MILLIS).addZero()
        val seconds = (this % MINUTE_IN_MILLIS / SECOND_IN_MILLIS).addZero()
        "$hours:$minutes:$seconds"
    } ?: "00:00"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()

@BindingAdapter(value = ["min", "max", "isWrapped"])
fun NumberPicker.setup(
    min: Int,
    max: Int,
    isWrapped: Boolean
) {
    minValue = min
    maxValue = if (max < min) min else max
    wrapSelectorWheel = isWrapped
}

@BindingAdapter(value = ["suffix"])
fun NumberPicker.configureFormatter(suffix: String) = setFormatter { value -> "$value $suffix" }
