package knaufdan.android.core.navigation

import android.os.Bundle

interface INavigator {

    fun navigateTo(
        pageNumber: Int,
        addToBackStack: Boolean,
        bundle: Bundle
    )

    fun backPressed()
}