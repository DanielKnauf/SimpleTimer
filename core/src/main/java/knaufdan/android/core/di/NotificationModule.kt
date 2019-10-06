package knaufdan.android.core.di

import dagger.Module
import dagger.Provides
import knaufdan.android.core.notification.INotificationService
import knaufdan.android.core.notification.NotificationService
import javax.inject.Singleton

@Module
class NotificationModule {

    @Singleton
    @Provides
    internal fun contributeNotificationService(notificationService: NotificationService): INotificationService =
        notificationService
}