package knaufdan.android.simpletimerapp.util.notification

interface INotificationService {

    fun configure(adjust: INotificationServiceConfig.() -> Unit)

    fun sendNotification(notificationStyle: NotificationStyle)
}
