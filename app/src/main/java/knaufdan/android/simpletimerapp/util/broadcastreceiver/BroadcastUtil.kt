package knaufdan.android.simpletimerapp.util.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject

class BroadcastUtil @Inject constructor(private val contextProvider: ContextProvider) {

    fun registerBroadcastReceiver(broadcastReceiver: ActionBroadcastReceiver) {
        IntentFilter()
            .apply {
                for (action: String in broadcastReceiver.getSupportedActions()) {
                    addAction(action)
                }
            }.also {
                LocalBroadcastManager.getInstance(contextProvider.context).registerReceiver(broadcastReceiver, it)
            }
    }

    fun unregisterBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(contextProvider.context).unregisterReceiver(broadcastReceiver)
    }
}