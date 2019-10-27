package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.core.arch.BaseFragment
import knaufdan.android.core.arch.ViewConfig
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.util.UnBoxUtil.safeUnBox

class TimerFragment : BaseFragment<TimerFragmentViewModel>() {

    override fun configureView(): ViewConfig = ViewConfig.Builder()
        .setLayoutRes(R.layout.timer_fragment)
        .setViewModelKey(BR.viewModel)
        .build()

    override fun onPause() {
        super.onPause()

        viewModel.stopReceivingUpdates()

        if (doNotSetUpAlarm()) {
            return
        }

        viewModel.setUpAlarm()
    }

    private fun doNotSetUpAlarm() =
        isBackPressed ||
                viewModel.isFinished() ||
                safeUnBox(viewModel.isPaused.value)

    override fun onResume() {
        super.onResume()

        if (safeUnBox(viewModel.isPaused.value)) {
            return
        }

        viewModel.restart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.releaseResources()
    }
}
