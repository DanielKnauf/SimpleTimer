package knaufdan.android.core.databinding

typealias LayoutRes = Int
typealias BindingKey = Int

interface Bindable<DataSource> {
    fun getDataSource(): DataSource

    fun getLayoutRes(): LayoutRes

    fun getBindingKey(): BindingKey
}