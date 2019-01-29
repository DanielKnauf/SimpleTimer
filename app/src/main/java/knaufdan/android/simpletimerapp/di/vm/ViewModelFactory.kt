package knaufdan.android.simpletimerapp.di.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
        ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var vm: Provider<out ViewModel>? = viewModels[modelClass]
        if (vm == null) {
            for ((key, value) in viewModels) {
                if (modelClass.isAssignableFrom(key)) {
                    vm = value
                    break
                }
            }
        }
        if (vm == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        try {
            return vm.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}
