package knaufdan.android.simpletimerapp.arch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import knaufdan.android.simpletimerapp.di.vm.ViewModelFactory

abstract class BaseFragment<V : ViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    protected abstract fun configureView(): ViewConfig

    var isBackPressed = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val config = configureView()

        checkNotNull(config.layoutRes) {
            "Activity parameters for " + javaClass.name + " have no layout resource."
        }

        checkNotNull(config.viewModelKey) {
            "Activity parameters for " + javaClass.name + " have no viewModel key."
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(typeOfViewModel)

        if (viewModel is BaseViewModel &&
            // do only initiate view model on first start
            savedInstanceState == null
        ) {
            (viewModel as BaseViewModel).init(arguments)
        }

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                inflater,
                config.layoutRes,
                container,
                false
            )

        return binding.run {
            lifecycleOwner = this@BaseFragment
            setVariable(config.viewModelKey, viewModel)
            executePendingBindings()
            binding.root
        }
    }

    override fun onResume() {
        super.onResume()
        isBackPressed = false
    }

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V>
        get() = (javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<V>
}
