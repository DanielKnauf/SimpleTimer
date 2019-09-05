package knaufdan.android.simpletimerapp.util

import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextProvider @Inject constructor(private val contextProvider: ContextProvider) {

    fun getText(@StringRes textId: Int, args: Any): String =
        contextProvider.context.getString(textId, args)

    fun getText(@StringRes textId: Int): String = contextProvider.context.getString(textId)

}