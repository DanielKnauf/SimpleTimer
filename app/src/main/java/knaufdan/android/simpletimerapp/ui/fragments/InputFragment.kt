package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R
import knaufdan.android.simpletimerapp.arch.BaseFragment
import knaufdan.android.simpletimerapp.arch.Config

class InputFragment : BaseFragment<InputFragmentViewModel>() {
    override fun activityParameters(): Config =
            Config.Builder()
                    .setLayoutRes(R.layout.fragment_main)
                    .setViewModelKey(BR.viewModel)
                    .build()
}