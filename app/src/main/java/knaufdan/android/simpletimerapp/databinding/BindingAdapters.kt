package knaufdan.android.simpletimerapp.databinding

import android.R
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.SECOND

@BindingAdapter(value = ["progressText"])
fun TextView.setProgressText(progress: Int?) {
    text = progress?.run {
        val minutes = (this / MINUTE).addZero()
        val seconds = (this % MINUTE / SECOND).addZero()
        "$minutes:$seconds"
    } ?: "00:00"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()

@BindingAdapter(value = ["itemSource"])
fun AppCompatSpinner.setArrayAdapter(itemSource: List<*>) {
    ArrayAdapter(
        context,
        R.layout.simple_spinner_item,
        itemSource
    ).also { adapter ->
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["currentSelection"])
fun AppCompatSpinner.setCurrentSelection(currentSelection: Int = 0) =
    this.setSelection(currentSelection)

