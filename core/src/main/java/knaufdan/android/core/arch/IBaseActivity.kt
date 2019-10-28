package knaufdan.android.core.arch

import androidx.fragment.app.FragmentManager

interface IBaseActivity {

    fun configureView(): ViewConfig

    /**
     * Communicates [onBackPressed] event to all [BaseViewModel] of [BaseFragment].
     */
    fun FragmentManager.notifyBackPressed()
}