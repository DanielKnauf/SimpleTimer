package knaufdan.android.simpletimerapp.util.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import knaufdan.android.simpletimerapp.util.ContextProvider
import javax.inject.Inject
import kotlin.reflect.KClass

class ServiceUtil @Inject constructor(private val contextProvider: ContextProvider) {

    fun <S : Service> startService(clazz: KClass<S>, bundle: Bundle?) {
        val context = contextProvider.context
        val intent = Intent(context, clazz.java)

        bundle?.let {
            intent.putExtras(it)
        }

        context.startService(intent)
    }

    fun <S : Service> stopService(clazz: KClass<S>) {
        val context = contextProvider.context
        context.stopService(Intent(context, clazz.java))
    }
}