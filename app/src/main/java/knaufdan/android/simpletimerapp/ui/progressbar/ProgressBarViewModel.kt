package knaufdan.android.simpletimerapp.ui.progressbar

import androidx.lifecycle.MutableLiveData

interface ProgressBarViewModel {

    fun getProgressMax():MutableLiveData<Int>

    fun getProgress():MutableLiveData<Int>
}