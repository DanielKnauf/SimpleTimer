package knaufdan.android.core.arch

import knaufdan.android.core.arch.implementation.BaseViewModel
import knaufdan.android.core.databinding.BindableElement

interface IBaseFragment<ViewModel : BaseViewModel> : BindableElement<ViewModel> {
    fun getTitleRes(): Int = -1

    fun setBackPressed(isBackPressed: Boolean)
}
