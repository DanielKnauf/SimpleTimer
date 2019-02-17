package knaufdan.android.simpletimerapp.arch

import android.os.Bundle

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected var tag: String = this.javaClass.name

    open fun init(bundle: Bundle?) {
        //can be overwritten
    }
}