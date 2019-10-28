package knaufdan.android.core.arch

import androidx.fragment.app.FragmentManager

interface IBaseActivity {

    fun configureView(): ViewConfig

    /**
     * Communicates an [onBackPressed] event to all [BaseViewModel] of [BaseFragment].
     */
    fun FragmentManager.notifyBackPressed()
}
