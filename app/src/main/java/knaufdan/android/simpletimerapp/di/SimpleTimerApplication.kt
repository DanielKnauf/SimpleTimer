package knaufdan.android.simpletimerapp.di

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import knaufdan.android.simpletimerapp.util.ContextProvider

class SimpleTimerApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector,
    HasBroadcastReceiverInjector {

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

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverInjector
}
