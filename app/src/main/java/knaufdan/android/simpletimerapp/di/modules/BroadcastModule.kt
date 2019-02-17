package knaufdan.android.simpletimerapp.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import knaufdan.android.simpletimerapp.util.alarm.AlarmReceiver

@Module
abstract class BroadcastModule {

    @ContributesAndroidInjector
    internal abstract fun contributeAlarmReceiver(): AlarmReceiver
}