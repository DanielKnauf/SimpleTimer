package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseFragment
import knaufdan.android.simpletimerapp.arch.Config

class TimerFragment : BaseFragment<TimerFragmentViewModel>() {

    override fun activityParameters(): Config = Config.Builder()
            .setLayoutRes(R.layout.timer_fragment)
            .setViewModelKey(BR.viewModel)
            .build()
}