package knaufdan.android.core.navigation

import androidx.annotation.LayoutRes
import knaufdan.android.core.arch.implementation.BaseFragment

interface INavigator {
    var fragmentContainer : Int

    fun configure(@LayoutRes fragmentContainer: Int)

    fun goTo(
        fragment: BaseFragment<*>,
        addToBackStack: Boolean,
        container: Int = fragmentContainer
    )

    fun backPressed()
}