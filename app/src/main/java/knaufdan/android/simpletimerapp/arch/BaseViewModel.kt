package knaufdan.android.simpletimerapp.arch

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected var className: String? = this::class.simpleName

    open fun init(bundle: Bundle?) {
        // empty body for inheritance
    }
}
