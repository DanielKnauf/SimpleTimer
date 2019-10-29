package knaufdan.android.core.arch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import knaufdan.android.core.di.vm.ViewModelFactory
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel> : Fragment(), IBaseFragment {

    val fragmentTag = this::class.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(typeOfViewModel)

        lifecycle.addObserver(viewModel)

        setBackPressed(isBackPressed = false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = configureView().run {
        checkNotNull(layoutRes) {
            "Activity parameters for " + javaClass.name + " have no layout resource."
        }

        checkNotNull(viewModelKey) {
            "Activity parameters for " + javaClass.name + " have no viewModel key."
        }

        if ( // do only initiate view model on first start
            savedInstanceState == null) {
            viewModel.handleBundle(arguments)
        }

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(
                inflater,
                layoutRes,
                container,
                false
            )

        return binding.run {
            lifecycleOwner = this@BaseFragment
            setVariable(viewModelKey, viewModel)
            executePendingBindings()
            binding.root
        }
    }

    override fun setBackPressed(isBackPressed: Boolean) {
        viewModel.isBackPressed = isBackPressed
    }

    @Suppress("UNCHECKED_CAST")
    private val typeOfViewModel: Class<V>
        get() = (javaClass
            .genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<V>
}
