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
import knaufdan.android.simpletimerapp.di.vm.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<V : ViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val config = activityParameters()

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(typeOfViewModel)

        if (viewModel is BaseViewModel
                //do only initiate view model on first start
                && savedInstanceState == null
        ) {
            (viewModel as BaseViewModel).init(arguments)
        }

        if (config.layoutRes == null) {
            throw IllegalArgumentException("Activity parameters for " + javaClass.name + " have no layout resource.")
        } else if (config.viewModelKey == null) {
            throw IllegalArgumentException("Activity parameters for " + javaClass.name + " have no viewModel key.")
        }

        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, config.layoutRes, container, false)

        binding.setLifecycleOwner(this)

        binding.setVariable(config.viewModelKey, viewModel)

        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected abstract fun activityParameters(): Config

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V>
        get() = (javaClass
                .genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<V>
}
