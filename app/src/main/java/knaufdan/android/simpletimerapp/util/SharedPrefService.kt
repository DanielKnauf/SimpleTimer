package knaufdan.android.simpletimerapp.util

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefService @Inject constructor(private val contextProvider: ContextProvider) {

    private val sharedPrefLocation = "knaufdan.android.simpletimerapp.sharedPref"

    fun saveTo(key: String, value: Any?) {
        val sharedPref = getSharedPref()

        with(sharedPref.edit()) {
            value?.let {
                putValue(it, key)
                apply()
            }
        }
    }

    private fun SharedPreferences.Editor.putValue(value: Any, key: String) {
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Enum<*> -> putString(key, value.name)
        }
    }

    fun retrieveString(key: String): String? = getSharedPref().getString(key, "")

    fun retrieveLong(key: String) = getSharedPref().getLong(key, 0)

    fun retrieveInt(key: String)= getSharedPref().getInt(key, 0)

    private fun getSharedPref() = contextProvider.context.getSharedPreferences(sharedPrefLocation, MODE_PRIVATE)
}
