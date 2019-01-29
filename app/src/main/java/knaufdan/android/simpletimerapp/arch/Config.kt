package knaufdan.android.simpletimerapp.arch

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

class Config(
        @LayoutRes
        val layoutRes: Int?,
        val viewModelKey: Int?,
        @StringRes
        val titleRes: Int?
) {
    data class Builder(
            var layoutRes: Int? = null,
            var viewModelKey: Int? = null,
            var titleRes: Int? = null
    ) {
        fun setLayoutRes(layoutRes: Int) = apply { this.layoutRes = layoutRes }

        fun setViewModelKey(viewModelKey: Int) = apply { this.viewModelKey = viewModelKey }

        fun setTitleRes(titleRes: Int) = apply { this.titleRes = titleRes }

        fun build() = Config(layoutRes, viewModelKey, titleRes)
    }
}