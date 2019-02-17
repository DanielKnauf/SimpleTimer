package knaufdan.android.simpletimerapp.util.broadcastreceiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class UpdateReceiver<E : Enum<E>>(
    forAction: Array<E>,
    private val actor: (action: String, extras: Bundle?) -> Unit
) : ActionBroadcastReceiver(forAction.map { it.name }) {

    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.evaluateAction(intent.extras) ?: logError("- no action set for this intent -")
    }

    private fun String.evaluateAction(extras: Bundle?) {
        if (forActions.contains(this)) {
            actor(this, extras)
        } else {
            logError("- action $this not set for this receiver -")
        }
    }

    private fun logError(msg: String) {
        Log.e(this.javaClass.simpleName, msg)
    }
}