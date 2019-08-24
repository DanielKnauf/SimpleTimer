package knaufdan.android.simpletimerapp.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import knaufdan.android.simpletimerapp.di.modules.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        ServiceModule::class,
        BroadcastModule::class
    ]
)
interface SimpleTimerComponent : AndroidInjector<SimpleTimerApplication>