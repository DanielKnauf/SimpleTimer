package knaufdan.android.core.arch.implementation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import knaufdan.android.core.ContextProvider
import knaufdan.android.core.arch.HasFragmentFlow
import knaufdan.android.core.arch.IBaseActivity
import knaufdan.android.core.di.vm.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<ViewModel : BaseViewModel> : AppCompatActivity(),
    IBaseActivity<ViewModel> {

    @Inject
    lateinit var contextProvider: ContextProvider

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ViewModel

    private var className: String? = this::class.simpleName

    private val config: Config.ActivityConfig by lazy {
        Config.ActivityConfig(
            layoutRes = getLayoutRes(),
            viewModelKey = getBindingKey(),
            titleRes = getTitleRes(),
            initialPage = getInitialPage()
        )
    }

    override fun getDataSource(): ViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        contextProvider.context = this

        config.run {
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

    private fun Config.setBinding(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this@BaseActivity, viewModelFactory).get(typeOfViewModel)

        lifecycle.addObserver(viewModel)

        // do only initiate view model on first start
        if (savedInstanceState == null) {
            viewModel.handleBundle(intent.extras)
        }

        DataBindingUtil.setContentView<ViewDataBinding>(this@BaseActivity, layoutRes).apply {
            lifecycleOwner = this@BaseActivity
            setVariable(viewModelKey, viewModel)
            executePendingBindings()
        }
    }

    private fun Config.ActivityConfig.showInitialPage(savedInstanceState: Bundle?) =
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
    private val typeOfViewModel: Class<ViewModel> =
        (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>
}
