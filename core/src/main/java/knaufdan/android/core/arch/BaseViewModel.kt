package knaufdan.android.core.arch

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), LifecycleObserver, IBaseViewModel {

    var isBackPressed = false

    override fun handleBundle(bundle: Bundle?) {
        // empty body for inheritance
    }
}
