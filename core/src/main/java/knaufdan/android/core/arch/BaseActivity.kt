package knaufdan.android.core.arch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import knaufdan.android.core.ContextProvider
import knaufdan.android.core.di.vm.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity(), IBaseActivity {

    @Inject
    lateinit var contextProvider: ContextProvider

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    private var className: String? = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        contextProvider.context = this
        configureView().run {
            setBinding(savedInstanceState)

            if (titleRes != -1) {
                setTitle(titleRes)
            }

            showInitialPage(savedInstanceState)
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.notifyBackPressed()
        super.onBackPressed()
    }

    override fun FragmentManager.notifyBackPressed() = fragments.forEach { fragment ->
        if (fragment is BaseFragment<*>) {
            fragment.setBackPressed(isBackPressed = true)
        }
    }

    private fun ViewConfig.setBinding(savedInstanceState: Bundle?) {
        checkNotNull(layoutRes) {
            "Activity parameters for $className have no layout resource."
        }

        checkNotNull(viewModelKey) {
            "Activity parameters for $className have no viewModel key."
        }

        viewModel = ViewModelProvider(this@BaseActivity, viewModelFactory).get(typeOfViewModel)

        lifecycle.addObserver(viewModel)

        if ( // do only initiate view model on first start
            savedInstanceState == null) {
            viewModel.handleBundle(intent.extras)
        }

        DataBindingUtil.setContentView<ViewDataBinding>(this@BaseActivity, layoutRes).apply {
            lifecycleOwner = this@BaseActivity
            setVariable(viewModelKey, viewModel)
            executePendingBindings()
        }
    }

    private fun ViewConfig.showInitialPage(savedInstanceState: Bundle?) =
        with(initialPage) {
            if (this >= 0) {
                if (this@BaseActivity is HasFragmentFlow) flowTo(
                    pageNumber = this,
                    addToBackStack = false,
                    bundle = savedInstanceState
                )
                else Log.e(
                    className,
                    "Found an initialPage to display (#$this), but $className does not implement " + HasFragmentFlow::class.simpleName
                )
            }
        }

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V> =
        (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<V>
}
