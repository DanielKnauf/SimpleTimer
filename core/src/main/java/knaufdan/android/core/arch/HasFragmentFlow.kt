package knaufdan.android.core.arch

import android.os.Bundle

interface HasFragmentFlow {

    fun flowTo(pageNumber: Int, addToBackStack: Boolean, bundle: Bundle? = null)
}
