package knaufdan.android.core.audio

import androidx.annotation.RawRes

interface IAudioService {
    fun initMediaPlayer(@RawRes mediaFile: Int)
    fun playSound()
    fun releaseMediaPlayer()
}