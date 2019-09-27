package knaufdan.android.simpletimerapp.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.tabs.TabLayout
import knaufdan.android.simpletimerapp.util.Constants.MINUTE_IN_MILLIS
import knaufdan.android.simpletimerapp.util.Constants.SECOND_IN_MILLIS

@BindingAdapter(value = ["progressText"])
fun TextView.setProgressText(progress: Int?) {
    text = progress?.run {
        val minutes = (this / MINUTE_IN_MILLIS).addZero()
        val seconds = (this % MINUTE_IN_MILLIS / SECOND_IN_MILLIS).addZero()
        "$minutes:$seconds"
    } ?: "Paused"
}

private fun Int.addZero() = if (this < 10) "0$this" else this.toString()

@BindingAdapter(value = ["itemSource", "onSelectedTab"])
fun TabLayout.populateFromSource(
    itemSource: List<String>,
    onSelectedTab: OnSelectedTab
) {
    removeAllTabs()

    itemSource.forEach { item ->
        newTab().apply {
            contentDescription = item
            text = item
            addTab(this)
        }
    }

    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // do nothing here
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // do nothing here
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onSelectedTab.onTabSelected(tab)
        }
    })
}

@BindingAdapter(value = ["currentSelection"])
fun TabLayout.setCurrentSelection(currentSelection: Int?) {
    currentSelection?.apply {
        this@setCurrentSelection.getTabAt(this)?.select()
    }
}

interface OnSelectedTab {
    fun onTabSelected(tab: TabLayout.Tab?)
}
