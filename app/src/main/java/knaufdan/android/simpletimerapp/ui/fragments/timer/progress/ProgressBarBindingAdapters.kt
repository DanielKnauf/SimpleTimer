package knaufdan.android.simpletimerapp.ui.fragments.timer.progress

import android.widget.TextView
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.determineClockSections

@BindingAdapter("progressText")
fun TextView.setProgressText(progress: Int?) {
    text =
        progress?.run {
            determineClockSections().run { "${first.addZero()}:${second.addZero()}:${third.addZero()}" }
        } ?: "00:00"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()
