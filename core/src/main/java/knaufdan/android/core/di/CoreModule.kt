package knaufdan.android.core.di

import dagger.Module
import dagger.Provides
import knaufdan.android.core.audio.AudioService
import knaufdan.android.core.audio.IAudioService
import knaufdan.android.core.broadcast.BroadcastService
import knaufdan.android.core.broadcast.IBroadcastService
import knaufdan.android.core.notification.INotificationService
import knaufdan.android.core.notification.NotificationService
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    internal fun provideNotificationService(notificationService: NotificationService): INotificationService =
        notificationService

    @Singleton
    @Provides
    internal fun provideAudioService(audioService: AudioService): IAudioService = audioService

    @Singleton
    @Provides
    internal fun provideBroadcastService(broadcastService: BroadcastService): IBroadcastService =
        broadcastService
}