package knaufdan.android.simpletimerapp.databinding

import androidx.lifecycle.MutableLiveData

class ExtMutableLiveData<T> constructor(initialValue: T) : MutableLiveData<T>() {

    init {
        value = initialValue
    }
}