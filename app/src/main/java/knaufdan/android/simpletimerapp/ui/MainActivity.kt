package knaufdan.android.simpletimerapp.ui

import knaufdan.android.core.SharedPrefService
import knaufdan.android.core.arch.implementation.BaseActivity
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragment
import knaufdan.android.simpletimerapp.util.Constants.KEY_TIMER_STATE
import knaufdan.android.simpletimerapp.util.service.TimerState
import javax.inject.Inject

class MainActivity : BaseActivity<MainActivityViewModel>() {

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    override fun getLayoutRes() = R.layout.activity_main

    override fun getBindingKey() = BR.viewModel

    override fun getFragmentSetup() = R.id.fragment_container to determineInitialFragment()

    override fun getTitleRes() = R.string.app_name

    private fun determineInitialFragment() =
        if (sharedPrefService.retrieveString(KEY_TIMER_STATE) == TimerState.RESTARTED_IN_BACKGROUND.name) TimerFragment()
        else InputFragment()
}
