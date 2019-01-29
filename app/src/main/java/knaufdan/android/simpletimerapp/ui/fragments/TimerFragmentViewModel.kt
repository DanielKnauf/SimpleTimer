package knaufdan.android.simpletimerapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import knaufdan.android.simpletimerapp.arch.BaseViewModel
import knaufdan.android.simpletimerapp.ui.navigation.Navigator
import knaufdan.android.simpletimerapp.ui.progressbar.ProgressBarViewModel
import knaufdan.android.simpletimerapp.util.Constants.MINUTE
import knaufdan.android.simpletimerapp.util.Constants.SECOND
import knaufdan.android.simpletimerapp.util.Constants.TIME_KEY
import javax.inject.Inject

class TimerFragmentViewModel @Inject constructor(private val navigator: Navigator) : BaseViewModel(), ProgressBarViewModel {


    val time: MutableLiveData<Int> = MutableLiveData()
    val proTime: MutableLiveData<Int> = MutableLiveData()

    init {
        proTime.value = 0
    }

    override fun init(bundle: Bundle?) {
        super.init(bundle)

        bundle?.let {
            time.value = it.getInt(TIME_KEY) * MINUTE
        }

        add()
    }

    private fun add() {
        Handler().postDelayed({
            proTime.postValue(proTime.value?.plus(SECOND))
            add()
        }, 3000)
    }

    override fun getProgressMax(): MutableLiveData<Int> = time

    override fun getProgress(): MutableLiveData<Int> = proTime
}