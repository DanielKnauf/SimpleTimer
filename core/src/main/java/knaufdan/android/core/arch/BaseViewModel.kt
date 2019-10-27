package knaufdan.android.core.arch

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open fun handleBundle(bundle: Bundle?) {
        // empty body for inheritance
    }
}
