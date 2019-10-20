package knaufdan.android.simpletimerapp.ui.navigation

import android.os.Bundle
import javax.inject.Inject
import javax.inject.Singleton
import knaufdan.android.simpletimerapp.arch.HasFragmentFlow
import knaufdan.android.simpletimerapp.ui.data.TimerConfiguration
import knaufdan.android.simpletimerapp.util.Constants.KEY_CURRENT_MAXIMUM
import knaufdan.android.simpletimerapp.util.Constants.KEY_IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.ContextProvider

@Singleton
class Navigator @Inject constructor(private val contextProvider: ContextProvider) {

    fun navigateToInput() {
        with(contextProvider.context) {
            if (this is HasFragmentFlow) {
                flowTo(
                    pageNumber = FragmentPage.INPUT.ordinal,
                    addToBackStack = false
                )
            }
        }
    }

    fun navigateToTimer(
        timerConfiguration: TimerConfiguration
    ) {
        with(contextProvider.context) {
            if (this is HasFragmentFlow) {
                flowTo(
                    pageNumber = FragmentPage.TIMER.ordinal,
                    addToBackStack = true,
                    bundle = Bundle()
                        .apply {
                            putInt(KEY_CURRENT_MAXIMUM, timerConfiguration.timePerCycle)
                            putBoolean(KEY_IS_ON_REPEAT, timerConfiguration.isOnRepeat)
                        }
                )
            }
        }
    }
}
