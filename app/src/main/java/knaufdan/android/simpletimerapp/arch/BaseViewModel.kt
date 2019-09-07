package knaufdan.android.simpletimerapp.arch

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected var className: String? = this::class.simpleName

    open fun init(bundle: Bundle?) {}

    fun <S, T> connect(
        source: LiveData<S>,
        target: MediatorLiveData<T>,
        connector: (sourceValue: S) -> T
    ) {
        target.addSource(source) { sourceValue ->
            val newValue = connector(sourceValue)

            if (target.value == newValue) {
                return@addSource
            }

            target.postValue(newValue)
        }
    }
}