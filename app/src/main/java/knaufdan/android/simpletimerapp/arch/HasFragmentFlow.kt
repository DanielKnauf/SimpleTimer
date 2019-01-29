package knaufdan.android.simpletimerapp.arch

import android.os.Bundle

interface HasFragmentFlow {

    fun flowTo(pageNumber: Int, addToBackStack: Boolean, bundle: Bundle?)
}