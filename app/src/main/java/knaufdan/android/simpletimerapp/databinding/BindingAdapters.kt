package knaufdan.android.simpletimerapp.databinding

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.determineClockSections

@BindingAdapter(value = ["progressText"])
fun TextView.setProgressText(progress: Int?) {
    text =
        progress?.run {
            determineClockSections().run { "${first.addZero()}:${second.addZero()}:${third.addZero()}" }
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
