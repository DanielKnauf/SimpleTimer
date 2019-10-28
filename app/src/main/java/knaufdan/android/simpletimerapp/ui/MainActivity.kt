package knaufdan.android.simpletimerapp.ui

import android.os.Bundle
import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.arch.BaseActivity
import knaufdan.android.core.arch.HasFragmentFlow
import knaufdan.android.core.arch.ViewConfig
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragment
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage.INPUT
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage.TIMER
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel>(), HasFragmentFlow {

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    override fun onResume() {
        super.onResume()

        if (hasTimerState(TimerState.FINISH_STATE)) {
            resetAppToStart()
        }
    }

    override fun flowTo(
        pageNumber: Int,
        addToBackStack: Boolean,
        bundle: Bundle?
    ) {
        with(supportFragmentManager.beginTransaction()) {
            pageNumber.determineFragment(bundle).run {
                replace(
                    R.id.fragment_container,
                    this,
                    this.fragmentTag
                )
            }

            if (addToBackStack) {
                addToBackStack(null)
            }

            commitAllowingStateLoss()
        }
    }

    private fun Int.determineFragment(bundle: Bundle?) = when (FragmentPage.values()[this]) {
        INPUT -> InputFragment()
        TIMER -> TimerFragment().apply { arguments = bundle }
    }

    override fun onBackPressed() = with(supportFragmentManager) {
        notifyBackPressed()
        if (backStackEntryCount == 0 && fragments[0]?.tag != InputFragment::class.simpleName) resetAppToStart()
        else super.onBackPressed()
    }

    private fun resetAppToStart() {
        supportFragmentManager.popBackStackImmediate()
        flowTo(INPUT.ordinal, false, null)
    }

    override fun configureView() =
        ViewConfig.Builder()
            .setLayoutRes(R.layout.activity_main)
            .setViewModelKey(BR.viewModel)
            .setTitleRes(R.string.app_name)
            .setInitialPage(determineInitialPage())
            .build()

    private fun determineInitialPage() =
        if (hasTimerState(TimerState.RESTARTED_IN_BACKGROUND)) TIMER
        else INPUT

    private fun hasTimerState(expectedState: TimerState) =
        sharedPrefService.retrieveString(KEY_TIMER_STATE) == expectedState.name
}
