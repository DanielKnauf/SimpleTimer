package knaufdan.android.simpletimerapp.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import knaufdan.android.simpletimerapp.ui.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity
}
