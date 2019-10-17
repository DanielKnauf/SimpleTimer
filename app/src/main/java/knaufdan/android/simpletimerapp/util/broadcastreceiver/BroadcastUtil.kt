package knaufdan.android.simpletimerapp.util.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import javax.inject.Inject
import knaufdan.android.core.ContextProvider

class BroadcastUtil @Inject constructor(private val contextProvider: ContextProvider) {

    fun registerBroadcastReceiver(broadcastReceiver: ActionBroadcastReceiver) {
        IntentFilter()
            .apply {
                for (action: String in broadcastReceiver.getSupportedActions()) {
                    addAction(action)
                }

                LocalBroadcastManager.getInstance(contextProvider.context).registerReceiver(broadcastReceiver, this)
            }
    }

    fun unregisterBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        LocalBroadcastManager.getInstance(contextProvider.context).unregisterReceiver(broadcastReceiver)
    }
}
