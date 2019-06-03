package knaufdan.android.simpletimerapp.arch

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import knaufdan.android.simpletimerapp.R

class ViewConfig(
    @LayoutRes val layoutRes: Int?,
    val viewModelKey: Int?,
    @StringRes val titleRes: Int,
    val initialPage: Int
) {
    data class Builder(
        var layoutRes: Int? = null,
        var viewModelKey: Int? = null,
        var titleRes: Int = R.string.app_name,
        var initialPage: Int = -1
    ) {
        fun setLayoutRes(layoutRes: Int) = apply { this.layoutRes = layoutRes }

        fun setViewModelKey(viewModelKey: Int) = apply { this.viewModelKey = viewModelKey }

        fun setTitleRes(titleRes: Int) = apply { this.titleRes = titleRes }

        fun setInitialPage(initialPage: Enum<*>) = setInitialPage(initialPage.ordinal)

        fun setInitialPage(initialPage: Int) = apply { this.initialPage = initialPage }

        fun build() = ViewConfig(layoutRes, viewModelKey, titleRes, initialPage)
    }
}