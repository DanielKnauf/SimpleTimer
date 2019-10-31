package knaufdan.android.core.arch.implementation

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

internal sealed class Config(
    @LayoutRes val layoutRes: Int,
    val viewModelKey: Int,
    @StringRes val titleRes: Int
) {
    internal class ActivityConfig(
        layoutRes: Int,
        viewModelKey: Int,
        titleRes: Int,
        val initialFragment: BaseFragment<*>? = null,
        val initialFragmentContainer: Int? = null
    ) : Config(
        layoutRes,
        viewModelKey,
        titleRes
    )

    internal class FragmentConfig(
        layoutRes: Int,
        viewModelKey: Int,
        titleRes: Int
    ) : Config(
        layoutRes,
        viewModelKey,
        titleRes
    )
}
