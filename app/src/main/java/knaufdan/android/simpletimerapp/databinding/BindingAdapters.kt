package knaufdan.android.simpletimerapp.databinding

import android.R
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.Constants.MINUTE_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS

@BindingAdapter(value = ["progressText"])
fun TextView.setProgressText(progress: Int?) {
    text = progress?.run {
        val minutes = (this / MINUTE_IN_MILLIS).addZero()
        val seconds = (this % MINUTE_IN_MILLIS / SECOND_IN_MILLIS).addZero()
        "$minutes:$seconds"
    } ?: "00:00"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()

@BindingAdapter(value = ["itemSource", "currentSelection"], requireAll = true)
fun AppCompatSpinner.setArrayAdapter(itemSource: List<*>, currentSelection: Int?) {
    ArrayAdapter(
        context,
        R.layout.simple_spinner_item,
        itemSource
    ).also { adapter ->
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }

    setCurrentSelection(currentSelection)
}

@BindingAdapter(value = ["currentSelection"])
fun AppCompatSpinner.setCurrentSelection(currentSelection: Int?) {
    currentSelection?.apply {
        this@setCurrentSelection.setSelection(this)
    }
}

