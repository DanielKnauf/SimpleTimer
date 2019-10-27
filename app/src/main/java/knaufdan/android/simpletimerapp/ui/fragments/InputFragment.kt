package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.core.arch.BaseFragment
import knaufdan.android.core.arch.ViewConfig
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R

class InputFragment : BaseFragment<InputFragmentViewModel>() {

    override fun onResume() {
        super.onResume()
        viewModel.resetState()
    }

    override fun configureView(): ViewConfig =
        ViewConfig.Builder()
            .setLayoutRes(R.layout.input_fragment)
            .setViewModelKey(BR.viewModel)
            .build()
}
