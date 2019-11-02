package knaufdan.android.core.arch.implementation

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import knaufdan.android.core.arch.FragmentContainer
import knaufdan.android.core.arch.InitialFragment

internal sealed class Config(
    @LayoutRes val layoutRes: Int,
    val viewModelKey: Int,
    @StringRes val titleRes: Int
) {
    internal class ActivityConfig(
        layoutRes: Int,
        viewModelKey: Int,
        titleRes: Int,
        val setup: Pair<FragmentContainer, InitialFragment?>?
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
