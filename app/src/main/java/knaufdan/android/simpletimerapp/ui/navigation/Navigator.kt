package knaufdan.android.simpletimerapp.ui.navigation

import android.os.Bundle
import knaufdan.android.simpletimerapp.arch.HasFragmentFlow
import knaufdan.android.simpletimerapp.util.Constants.END_TIME_KEY
import knaufdan.android.simpletimerapp.util.Constants.IS_ON_REPEAT
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val contextProvider: ContextProvider) {

    fun navigateToInput() {
        with(contextProvider.context) {
            if (this is HasFragmentFlow) {
                flowTo(FragmentPage.INPUT.ordinal, false, null)
            }
        }
    }

    fun navigateToTimer(
        endTimeInMinutes: Int,
        isOnRepeat: Boolean = false
    ) {
        with(contextProvider.context) {
            if (this is HasFragmentFlow) {
                flowTo(
                    FragmentPage.TIMER.ordinal,
                    true,
                    Bundle()
                        .apply {
                            putInt(END_TIME_KEY, endTimeInMinutes)
                            putBoolean(IS_ON_REPEAT, isOnRepeat)
                        }
                )
            }
        }
    }
}