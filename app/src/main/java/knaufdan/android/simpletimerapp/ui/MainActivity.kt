package knaufdan.android.simpletimerapp.ui

import android.os.Bundle
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseActivity
import knaufdan.android.simpletimerapp.arch.Config
import knaufdan.android.simpletimerapp.arch.HasFragmentFlow
import knaufdan.android.simpletimerapp.ui.fragments.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragment
import knaufdan.android.simpletimerapp.ui.navigation.FragmentPage

class MainActivity : BaseActivity<MainActivityViewModel>(), HasFragmentFlow {

    override fun flowTo(pageNumber: Int, addToBackStack: Boolean, bundle: Bundle?) {
        val page = FragmentPage.values()[pageNumber]
        val ft = supportFragmentManager.beginTransaction()

        when (page) {
            FragmentPage.INPUT -> ft.replace(R.id.fragment_container, InputFragment())
            FragmentPage.TIMER -> {
                ft.replace(
                    R.id.fragment_container,
                    TimerFragment()
                        .apply { arguments = bundle }
                )
            }
        }

        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowTo(FragmentPage.INPUT.ordinal, false, savedInstanceState)
    }

    override fun activityParameters() =
        Config.Builder()
            .setLayoutRes(R.layout.activity_main)
            .setViewModelKey(BR.viewModel)
            .setTitleRes(R.string.app_name)
            .build()
}

