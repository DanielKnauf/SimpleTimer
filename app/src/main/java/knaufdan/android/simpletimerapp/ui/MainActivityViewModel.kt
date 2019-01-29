package knaufdan.android.simpletimerapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

    val name: MutableLiveData<String> = MutableLiveData()

    init {
        name.value = "New Project :: start"
    }
}