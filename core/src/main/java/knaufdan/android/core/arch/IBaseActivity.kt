package knaufdan.android.core.arch

import androidx.fragment.app.FragmentManager
import knaufdan.android.core.arch.implementation.BaseViewModel
import knaufdan.android.core.databinding.Bindable

interface IBaseActivity<ViewModel : BaseViewModel> : Bindable<ViewModel> {
    fun getInitialPage(): Int = -1

    fun getTitleRes(): Int = -1

    /**
     * Communicates an [onBackPressed] event to all [BaseViewModel] of [BaseFragment].
     */
    fun FragmentManager.notifyBackPressed()
}
