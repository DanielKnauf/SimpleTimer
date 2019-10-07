package knaufdan.android.simpletimerapp.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject

class SimpleTimerApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    @Inject
    internal lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    @Inject
    internal lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    internal lateinit var contextProvider: ContextProvider

    override fun onCreate() {
        super.onCreate()
        DaggerSimpleTimerComponent
            .create()
            .inject(this)

        contextProvider.context = applicationContext
    }
}
