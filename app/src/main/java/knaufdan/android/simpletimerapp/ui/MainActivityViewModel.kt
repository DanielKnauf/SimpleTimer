package knaufdan.android.simpletimerapp.ui

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import javax.inject.Inject
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.arch.implementation.ActivityViewModel
import knaufdan.android.core.navigation.INavigationService
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.util.Constants
import knaufdan.android.simpletimerapp.util.service.TimerState

class MainActivityViewModel @Inject constructor(
    private val sharedPrefService: SharedPrefService,
    private val navigationService: INavigationService
) : ActivityViewModel() {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        if (sharedPrefService.retrieveString(Constants.KEY_TIMER_STATE) == TimerState.FINISH_STATE.name) {
            resetAppToStart()
        }
    }

    override fun handleBackPressed(fragmentManager: FragmentManager) =
        fragmentManager.run {
            if (backStackEntryCount == 0 && fragments[0]?.tag != InputFragment::class.simpleName) {
                resetAppToStart()
                true
            } else {
                false
            }
        }

    private fun resetAppToStart() {
        navigationService.cleanGoTo(fragment = InputFragment())
    }
}
