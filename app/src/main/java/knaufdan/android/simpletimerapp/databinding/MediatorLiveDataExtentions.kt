package knaufdan.android.simpletimerapp.databinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, S> MediatorLiveData<T>.bindTo(
    source: LiveData<S>,
    postOnlyDifferentValues: Boolean = true,
    mapping: (sourceValue: S) -> T = { s ->
        @Suppress("UNCHECKED_CAST")
        s as T
    }
) {
    addSource(source) { sourceValue ->
        val newValue = mapping(sourceValue)

        postValue(
            postOnlyDifferentValues = postOnlyDifferentValues,
            newValue = newValue
        )
    }
}

fun <T, S, U, V> MediatorLiveData<T>.bindTo(
    source1: LiveData<S>,
    source2: LiveData<U>,
    source3: LiveData<V>,
    postOnlyDifferentValues: Boolean = true,
    mapping: (sourceValue1: S?, sourceValue2: U?, sourceValue3: V?) -> T
) {
    addSource(source1) { sourceValue1 ->
        val newValue = mapping(
            sourceValue1,
            source2.value,
            source3.value
        )

        postValue(
            postOnlyDifferentValues = postOnlyDifferentValues,
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
            postOnlyDifferentValues = postOnlyDifferentValues,
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
            postOnlyDifferentValues = postOnlyDifferentValues,
            newValue = newValue
        )
    }
}

private fun <T> MediatorLiveData<T>.postValue(
    postOnlyDifferentValues: Boolean,
    newValue: T
) {
    if (postOnlyDifferentValues && value == newValue) {
        return
    }

    postValue(newValue)
}
