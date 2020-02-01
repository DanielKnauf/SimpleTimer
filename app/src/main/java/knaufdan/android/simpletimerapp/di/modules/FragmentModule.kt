package knaufdan.android.simpletimerapp.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import knaufdan.android.simpletimerapp.ui.fragments.input.InputFragment
import knaufdan.android.simpletimerapp.ui.fragments.timer.TimerFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeInputFragment(): InputFragment

    @ContributesAndroidInjector
    internal abstract fun contributeTimerFragment(): TimerFragment
}
