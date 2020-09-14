package knaufdan.android.simpletimerapp.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.ITimeSelectorComponentFactory
import knaufdan.android.simpletimerapp.ui.fragments.input.timeSelector.implementation.TimeSelectorComponentFactory

@Module
class ComponentFactoryModule {

    @Provides
    @Singleton
    fun provideTimeSelectorComponentFactory(): ITimeSelectorComponentFactory = TimeSelectorComponentFactory()
}
