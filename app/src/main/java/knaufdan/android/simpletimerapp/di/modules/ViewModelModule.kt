package knaufdan.android.simpletimerapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import knaufdan.android.core.di.vm.ViewModelFactory
import knaufdan.android.core.di.vm.ViewModelKey
import knaufdan.android.simpletimerapp.ui.MainActivityViewModel
import knaufdan.android.simpletimerapp.ui.fragments.InputFragmentViewModel
import knaufdan.android.simpletimerapp.ui.fragments.TimerFragmentViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InputFragmentViewModel::class)
    internal abstract fun bindInputFragmentViewModel(mainFragmentViewModel: InputFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimerFragmentViewModel::class)
    internal abstract fun bindTimerFragmentViewModel(timerFragmentViewModel: TimerFragmentViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
