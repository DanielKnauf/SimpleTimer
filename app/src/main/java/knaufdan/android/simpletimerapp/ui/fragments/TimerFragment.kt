package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.core.arch.implementation.BaseFragment
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R

class TimerFragment : BaseFragment<TimerFragmentViewModel>() {
    override fun getBindingKey() = BR.viewModel

    override fun getLayoutRes() = R.layout.timer_fragment
}
