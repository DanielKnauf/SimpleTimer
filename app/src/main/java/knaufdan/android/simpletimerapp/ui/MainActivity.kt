package knaufdan.android.simpletimerapp.ui

import android.os.Bundle
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseActivity
import knaufdan.android.simpletimerapp.arch.BaseFragment
import knaufdan.android.simpletimerapp.arch.HasFragmentFlow
import knaufdan.android.simpletimerapp.arch.ViewConfig
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragment
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage.INPUT
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage.TIMER
import knaufdan.android.simpletimerapp.util.Constants.STATE_KEY
import knaufdan.android.simpletimerapp.util.SharedPrefService
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel>(), HasFragmentFlow {

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    override fun onResume() {
        super.onResume()

        val state = sharedPrefService.retrieveString(STATE_KEY)

        if (TimerState.FINISH_STATE.name == state) {
            supportFragmentManager.popBackStackImmediate()
            flowTo(INPUT.ordinal, false, null)
        }
    }

    override fun flowTo(pageNumber: Int, addToBackStack: Boolean, bundle: Bundle?) {
        val page = FragmentPage.values()[pageNumber]
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, determineFragment(page, bundle))

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun determineFragment(page: FragmentPage, bundle: Bundle?) =
        when (page) {
            INPUT -> InputFragment()
            TIMER -> TimerFragment().apply { arguments = bundle }
        }

    override fun onBackPressed() {
        supportFragmentManager.fragments[0]?.let {
            if (it is BaseFragment<*>) {
                it.onBackPress()
            }
        }

        super.onBackPressed()
    }

    override fun configureView() =
        ViewConfig.Builder()
            .setLayoutRes(R.layout.activity_main)
            .setViewModelKey(BR.viewModel)
            .setTitleRes(R.string.app_name)
            .setInitialPage(INPUT)
            .build()
}

