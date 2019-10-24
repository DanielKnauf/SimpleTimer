package knaufdan.android.core.audio

import androidx.annotation.RawRes

interface IAudioService {

    /**
     * Plays the [soundFileRes].
     * If music is playing, the [AudioFocus] is requested with durationHint [AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK]
     */
    fun play(@RawRes soundFileRes: Int)

    /**
     * Releases all resources corresponding to this [soundFileRes].
     */
    fun release(@RawRes soundFileRes: Int)
}
