package knaufdan.android.core.arch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import knaufdan.android.core.ContextProvider
import knaufdan.android.core.di.vm.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<V : ViewModel> : AppCompatActivity() {

    @Inject
    lateinit var contextProvider: ContextProvider

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    protected abstract fun configureView(): ViewConfig

    private var className: String? = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val viewConfig = configureView()

        contextProvider.context = this

        viewConfig.setBinding(savedInstanceState)

        if (viewConfig.titleRes != -1) {
            setTitle(viewConfig.titleRes)
        }

        showInitialPage(viewConfig, savedInstanceState)
    }

    private fun ViewConfig.setBinding(
        savedInstanceState: Bundle?
    ) {
        checkNotNull(layoutRes) {
            "Activity parameters for $className have no layout resource."
        }

        checkNotNull(viewModelKey) {
            "Activity parameters for $className have no viewModel key."
        }

        viewModel = ViewModelProvider(this@BaseActivity, viewModelFactory).get(typeOfViewModel)

        if (viewModel is BaseViewModel &&
            // do only initiate view model on first start
            savedInstanceState == null
        ) {
            (viewModel as BaseViewModel).handleBundle(intent.extras)
        }

        DataBindingUtil.setContentView<ViewDataBinding>(this@BaseActivity, layoutRes).apply {
            lifecycleOwner = this@BaseActivity
            setVariable(viewModelKey, viewModel)
            executePendingBindings()
        }
    }

    private fun showInitialPage(
        viewConfig: ViewConfig,
        savedInstanceState: Bundle?
    ) {
        val initialPage = viewConfig.initialPage

        if (initialPage >= 0) {
            if (this is HasFragmentFlow) flowTo(initialPage, false, savedInstanceState)
            else Log.e(
                className,
                "Found an initialPage to display (#$initialPage), but $className does not implement " + HasFragmentFlow::class.simpleName
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V> =
        (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<V>
}
