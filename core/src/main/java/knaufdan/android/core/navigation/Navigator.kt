package knaufdan.android.core.navigation

import android.app.Activity
import android.os.Bundle
import knaufdan.android.core.ContextProvider
import knaufdan.android.core.arch.HasFragmentFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(private val contextProvider: ContextProvider) : INavigator {

    override fun navigateTo(
        pageNumber: Int,
        addToBackStack: Boolean,
        bundle: Bundle
    ) = with(contextProvider.context) {
        if (this is HasFragmentFlow) {
            flowTo(
                pageNumber = pageNumber,
                addToBackStack = addToBackStack,
                bundle = bundle
            )
        }
    }

    override fun backPressed() = with(contextProvider.context) {
        if (this is Activity) {
            onBackPressed()
        }
    }
}
