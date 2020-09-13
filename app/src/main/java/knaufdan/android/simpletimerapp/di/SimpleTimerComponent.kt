package knaufdan.android.simpletimerapp.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton
import knaufdan.android.arch.dagger.ArchModule
import knaufdan.android.services.dagger.ServiceModule
import knaufdan.android.simpletimerapp.di.modules.ActivityModule
import knaufdan.android.simpletimerapp.di.modules.AppServiceModule
import knaufdan.android.simpletimerapp.di.modules.BroadcastModule
import knaufdan.android.simpletimerapp.di.modules.ComponentFactoryModule
import knaufdan.android.simpletimerapp.di.modules.FragmentModule
import knaufdan.android.simpletimerapp.di.modules.ViewModelModule

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        AppServiceModule::class,
        BroadcastModule::class,
        ArchModule::class,
        ServiceModule::class,
        ComponentFactoryModule::class
    ]
)
interface SimpleTimerComponent : AndroidInjector<SimpleTimerApplication>
