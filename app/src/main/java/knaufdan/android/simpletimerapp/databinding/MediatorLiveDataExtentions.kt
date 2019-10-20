package knaufdan.android.simpletimerapp.databinding

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

        postValue(
            bindSafe = bindSafe,
            newValue = newValue
        )
    }
}

fun <T, S, U, V> MediatorLiveData<T>.bindTo(
    source1: LiveData<S>,
    source2: LiveData<U>,
    source3: LiveData<V>,
    bindSafe: Boolean = true,
    mapping: (sourceValue1: S?, sourceValue2: U?, sourceValue3: V?) -> T
) {
    addSource(source1) { sourceValue1 ->
        val newValue = mapping(
            sourceValue1,
            source2.value,
            source3.value
        )

        postValue(
            bindSafe = bindSafe,
            newValue = newValue
        )
    }

    addSource(source2) { sourceValue2 ->
        val newValue = mapping(
            source1.value,
            sourceValue2,
            source3.value
        )

        postValue(
            bindSafe = bindSafe,
            newValue = newValue
        )
    }

    addSource(source3) { sourceValue3 ->
        val newValue = mapping(
            source1.value,
            source2.value,
            sourceValue3
        )

        postValue(
            bindSafe = bindSafe,
            newValue = newValue
        )
    }
}

private fun <T> MediatorLiveData<T>.postValue(
    bindSafe: Boolean,
    newValue: T
) {
    if (bindSafe && value == newValue) {
        return
    }

    postValue(newValue)
}
