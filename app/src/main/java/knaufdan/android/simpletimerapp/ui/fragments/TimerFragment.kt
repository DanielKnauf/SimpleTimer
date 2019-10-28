package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.core.arch.BaseFragment
import knaufdan.android.core.arch.ViewConfig
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R

class TimerFragment : BaseFragment<TimerFragmentViewModel>() {
    override fun configureView(): ViewConfig = ViewConfig.Builder()
        .setLayoutRes(R.layout.timer_fragment)
        .setViewModelKey(BR.viewModel)
        .build()
}
