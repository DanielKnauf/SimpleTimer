package knaufdan.android.core.arch

import knaufdan.android.core.arch.implementation.BaseViewModel
import knaufdan.android.core.databinding.Bindable

interface IBaseFragment<ViewModel : BaseViewModel> : Bindable<ViewModel> {
    fun getTitleRes(): Int = -1

    fun setBackPressed(isBackPressed: Boolean)
}
