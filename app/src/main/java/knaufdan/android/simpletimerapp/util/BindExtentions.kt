package knaufdan.android.simpletimerapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, S> MediatorLiveData<T>.bindTo(
    source: LiveData<S>,
    bindSafe: Boolean = true,
    mapping: (sourceValue: S) -> T = { s ->
        @Suppress("UNCHECKED_CAST")
        s as T
    }
) {
    addSource(source) { sourceValue ->
        val newValue = mapping(sourceValue)

        if (bindSafe && value == newValue) {
            return@addSource
        }

        postValue(newValue)
    }
}
