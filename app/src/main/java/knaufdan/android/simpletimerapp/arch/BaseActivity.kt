package knaufdan.android.simpletimerapp.arch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import knaufdan.android.simpletimerapp.di.vm.ViewModelFactory
import knaufdan.android.simpletimerapp.util.ContextProvider
import java.lang.reflect.ParameterizedType
import javax.inject.Inject


abstract class BaseActivity<V : ViewModel> : AppCompatActivity() {

    @Inject
    lateinit var contextProvider: ContextProvider

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val activityParameters = activityParameters()

        contextProvider.context = this

        setBinding(activityParameters)

        activityParameters.titleRes?.let { setTitle(it) }
    }

    private fun setBinding(activityParameters: Config) {
        if (activityParameters.layoutRes == null) {
            throw IllegalArgumentException("Activity parameters for " + javaClass.name + " have no layout resource.")
        } else if (activityParameters.viewModelKey == null) {
            throw IllegalArgumentException("Activity parameters for " + javaClass.name + " have no viewModel key.")
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(typeOfViewModel)

        val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, activityParameters.layoutRes)
        binding.setLifecycleOwner(this)
        binding.setVariable(activityParameters.viewModelKey, viewModel)
    }

    protected abstract fun activityParameters(): Config

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V>
        get() = (javaClass
                .genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<V>
}
