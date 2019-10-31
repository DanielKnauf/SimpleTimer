package knaufdan.android.core.navigation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import knaufdan.android.core.ContextProvider
import knaufdan.android.core.arch.implementation.BaseFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val contextProvider: ContextProvider) : INavigator {
    override var fragmentContainer: Int = -1

    override fun configure(fragmentContainer: Int) {
        this.fragmentContainer = fragmentContainer
    }

    override fun goTo(
        fragment: BaseFragment<*>,
        addToBackStack: Boolean,
        container: Int
    ) = with(contextProvider.context) {

        check(container != -1) { "" }

        if (this is AppCompatActivity) {
            supportFragmentManager.beginTransaction().apply {
                replace(
                    container,
                    fragment,
                    fragment.fragmentTag
                )

                if (addToBackStack) {
                    addToBackStack(null)
                }

                commitAllowingStateLoss()
            }
        }
    }

    override fun backPressed() = with(contextProvider.context) {
        if (this is Activity) {
            onBackPressed()
        }
    }
}
