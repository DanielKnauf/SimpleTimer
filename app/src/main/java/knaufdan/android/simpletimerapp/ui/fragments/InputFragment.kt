package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseFragment
import knaufdan.android.simpletimerapp.arch.ViewConfig

class InputFragment : BaseFragment<InputFragmentViewModel>() {

    override fun configureView(): ViewConfig =
        ViewConfig.Builder()
            .setLayoutRes(R.layout.input_fragment)
            .setViewModelKey(BR.viewModel)
            .build()
}