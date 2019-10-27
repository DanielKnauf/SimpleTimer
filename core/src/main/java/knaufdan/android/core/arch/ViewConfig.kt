package knaufdan.android.core.arch

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

class ViewConfig(
    @LayoutRes val layoutRes: Int?,
    val viewModelKey: Int?,
    @StringRes val titleRes: Int,
    val initialPage: Int
) {
    data class Builder(
        var layoutRes: Int? = null,
        var viewModelKey: Int? = null,
        var titleRes: Int = -1,
        var initialPage: Int = -1
    ) {
        fun setLayoutRes(layoutRes: Int) = apply { this.layoutRes = layoutRes }

        fun setViewModelKey(viewModelKey: Int) = apply { this.viewModelKey = viewModelKey }

        fun setTitleRes(titleRes: Int) = apply { this.titleRes = titleRes }

        fun setInitialPage(initialPage: Enum<*>) = setInitialPage(initialPage.ordinal)

        private fun setInitialPage(initialPage: Int) = apply { this.initialPage = initialPage }

        fun build() = ViewConfig(layoutRes, viewModelKey, titleRes, initialPage)
    }
}
