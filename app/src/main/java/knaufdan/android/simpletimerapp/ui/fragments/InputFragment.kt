package knaufdan.android.simpletimerapp.ui.fragments

import knaufdan.android.arch.mvvm.implementation.BaseFragment
import knaufdan.android.simpletimerapp.BR
import knaufdan.android.simpletimerapp.R

class InputFragment : BaseFragment<InputFragmentViewModel>() {
    override fun getLayoutRes() = R.layout.input_fragment

    override fun getBindingKey() = BR.viewModel
}
