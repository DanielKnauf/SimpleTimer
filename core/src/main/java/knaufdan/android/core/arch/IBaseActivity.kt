package knaufdan.android.core.arch

import androidx.fragment.app.FragmentManager
import knaufdan.android.core.arch.implementation.BaseFragment
import knaufdan.android.core.arch.implementation.BaseViewModel
import knaufdan.android.core.databinding.BindableElement

interface IBaseActivity<ViewModel : BaseViewModel> : BindableElement<ViewModel> {
    fun getInitialFragment(): BaseFragment<*>? = null

    fun getInitialFragmentContainer(): Int? = null

    fun getTitleRes(): Int = -1

    /**
     * Communicates an [onBackPressed] event to all [BaseViewModel] of [BaseFragment].
     */
    fun FragmentManager.notifyBackPressed()
}
