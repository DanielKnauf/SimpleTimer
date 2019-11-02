package knaufdan.android.simpletimerapp.ui

import javax.inject.Inject
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.arch.implementation.BaseActivity
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragment
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState

class MainActivity : BaseActivity<MainActivityViewModel>() {

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    override fun getLayoutRes() = R.layout.activity_main

    override fun getBindingKey() = BR.viewModel

    override fun getFragmentSetup() = R.id.fragment_container to determineInitialFragment()

    override fun getTitleRes() = R.string.app_name

    override fun onResume() {
        super.onResume()

        if (hasTimerState(TimerState.FINISH_STATE)) {
            resetAppToStart()
        }
    }

    override fun onBackPressed() = with(supportFragmentManager) {
        notifyBackPressed()
        if (backStackEntryCount == 0 && fragments[0]?.tag != InputFragment::class.simpleName) resetAppToStart()
        else super.onBackPressed()
    }

    private fun resetAppToStart() {
        supportFragmentManager.popBackStackImmediate()
        navigator.goTo(
            fragment = InputFragment(),
            addToBackStack = false
        )
    }

    private fun determineInitialFragment() =
        if (hasTimerState(TimerState.RESTARTED_IN_BACKGROUND)) TimerFragment()
        else InputFragment()

    private fun hasTimerState(expectedState: TimerState) =
        sharedPrefService.retrieveString(KEY_TIMER_STATE) == expectedState.name
}
