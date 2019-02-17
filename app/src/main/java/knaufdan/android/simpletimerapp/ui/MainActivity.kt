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

class MainActivity : BaseActivity<MainActivityViewModel>(), HasFragmentFlow {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowTo(FragmentPage.INPUT.ordinal, false, savedInstanceState)
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
            FragmentPage.INPUT -> InputFragment()
            FragmentPage.TIMER -> TimerFragment().apply { arguments = bundle }
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
            .build()
}

