package knaufdan.android.simpletimerapp.util.broadcastreceiver

import android.content.BroadcastReceiver

abstract class ActionBroadcastReceiver(protected val forActions: List<String>) : BroadcastReceiver() {

    fun getSupportedActions(): List<String> = forActions
}
