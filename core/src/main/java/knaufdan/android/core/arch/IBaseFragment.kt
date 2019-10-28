package knaufdan.android.core.arch

interface IBaseFragment {
    fun configureView(): ViewConfig

    fun setBackPressed(isBackPressed: Boolean)
}