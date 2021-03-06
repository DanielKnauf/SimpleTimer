package knaufdan.android.simpletimerapp.ui

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import knaufdan.android.arch.mvvm.implementation.ActivityViewModel
import knaufdan.android.arch.navigation.INavigationService
import knaufdan.android.core.preferences.ISharedPrefService
import knaufdan.android.simpletimerapp.ui.fragments.input.InputFragment
import knaufdan.android.simpletimerapp.util.Constants
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val sharedPrefService: ISharedPrefService,
    private val navigationService: INavigationService
) : ActivityViewModel() {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        if (sharedPrefService.getString(Constants.KEY_TIMER_STATE) == TimerState.FINISH_STATE.name) {
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
        navigationService.goToFragment(
            fragment = InputFragment(),
            addToBackStack = false,
            clearBackStack = true
        )
    }
}
