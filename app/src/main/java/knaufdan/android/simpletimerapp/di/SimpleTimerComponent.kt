package knaufdan.android.simpletimerapp.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import knaufdan.android.core.di.NotificationModule
import knaufdan.android.simpletimerapp.di.modules.ActivityModule
import knaufdan.android.simpletimerapp.di.modules.BroadcastModule
import knaufdan.android.simpletimerapp.di.modules.FragmentModule
import knaufdan.android.simpletimerapp.di.modules.ServiceModule
import knaufdan.android.simpletimerapp.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        ServiceModule::class,
        BroadcastModule::class,
        NotificationModule::class
    ]
)
interface SimpleTimerComponent : AndroidInjector<SimpleTimerApplication>