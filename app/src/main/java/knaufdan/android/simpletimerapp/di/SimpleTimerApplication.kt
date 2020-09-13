package knaufdan.android.simpletimerapp.di

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import knaufdan.android.core.IContextProvider
import javax.inject.Inject

class SimpleTimerApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    @Inject
    internal lateinit var contextProvider: IContextProvider

    override fun onCreate() {
        super.onCreate()
        DaggerSimpleTimerComponent
            .create()
            .inject(this)

        contextProvider.setContext(applicationContext)
    }
}
