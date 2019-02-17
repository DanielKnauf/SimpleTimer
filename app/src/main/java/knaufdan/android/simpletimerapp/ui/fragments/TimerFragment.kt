package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseFragment
import knaufdan.android.simpletimerapp.arch.ViewConfig

class TimerFragment : BaseFragment<TimerFragmentViewModel>() {

    override fun configureView(): ViewConfig = ViewConfig.Builder()
            .setLayoutRes(R.layout.timer_fragment)
            .setViewModelKey(BR.viewModel)
            .build()

    override fun onPause() {
        super.onPause()

        viewModel.stopReceivingUpdates()

        if (!backPressed) {
            viewModel.setUpAlarm()
        }
    }
}