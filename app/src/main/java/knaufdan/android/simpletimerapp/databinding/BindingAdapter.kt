package knaufdan.android.simpletimerapp.databinding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.SECOND

object BindingAdapter {

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("progressText")
    fun setProgressText(view: TextView, progress: Int?) {
        val minutes = progress?.let { addZero(it / MINUTE) } ?: "00"
        val seconds = progress?.let { addZero(it % MINUTE / SECOND) } ?: "00"
        view.text = "$minutes:$seconds"
    }

    private fun addZero(time: Int): String =
            if (time < 10) "0$time"
            else Integer.toString(time)
}