package knaufdan.android.simpletimerapp.databinding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.SECOND

@SuppressLint("SetTextI18n")
@BindingAdapter("progressText")
fun TextView.setProgressText(progress: Int?) {
    text = progress?.run {
        val minutes = (this / MINUTE).addZero()
        val seconds = (this % MINUTE / SECOND).addZero()
        "$minutes:$seconds"
    } ?: "00:00"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()