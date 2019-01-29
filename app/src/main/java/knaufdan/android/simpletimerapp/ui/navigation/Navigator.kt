package knaufdan.android.simpletimerapp.ui.navigation

import android.os.Bundle
import knaufdan.android.simpletimerapp.arch.HasFragmentFlow
import knaufdan.android.simpletimerapp.util.Constants.TIME_KEY
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val contextProvider: ContextProvider) {

    fun navigateToInput() {
        val context = contextProvider.context

        if (context is HasFragmentFlow) {
            context.flowTo(FragmentPage.INPUT.ordinal, false, null)
        }
    }

    fun navigateToTimer(cycleTime: Int) {
        val context = contextProvider.context

        val bundle = Bundle()
        bundle.putInt(TIME_KEY, cycleTime)

        if (context is HasFragmentFlow) {
            context.flowTo(FragmentPage.TIMER.ordinal, true, bundle)
        }
    }
}