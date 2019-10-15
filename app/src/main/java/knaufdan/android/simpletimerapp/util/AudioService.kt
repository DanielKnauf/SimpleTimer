package knaufdan.android.simpletimerapp.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import javax.inject.Inject
import knaufdan.android.simpletimerapp.R

class AudioService @Inject constructor(private val contextProvider: ContextProvider) {

    private val audioManager by lazy {
        contextProvider.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val audioAttributes by lazy {
        AudioAttributes.Builder().run {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            build()
        }
    }

    private val audioFocusChangeListener by lazy {
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            // loss of audio focus is indicated by a negative value
            if (focusChange < 0) mediaPlayer.stop()
        }
    }

    private val mediaPlayer by lazy {
        MediaPlayer.create(
            contextProvider.context,
            R.raw.gong_sound
        ).apply {
            setAudioAttributes(audioAttributes)
            setOnCompletionListener { releaseAudioFocus() }
        }
    }

    private fun MediaPlayer.releaseAudioFocus() {
        if (!isPlaying) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(
                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).run {
                        setAudioAttributes(audioAttributes)
                        build()
                    }
                )
            } else {
                audioManager.abandonAudioFocus(audioFocusChangeListener)
            }
        }
    }

    fun playGong() {
        audioManager.apply {
            if (isMusicActive) {
                val res: Int =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requestAudioFocus(
                            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).run {
                                setAudioAttributes(audioAttributes)
                                setOnAudioFocusChangeListener(audioFocusChangeListener)
                                build()
                            }
                        )
                    } else {
                        requestAudioFocus(
                            audioFocusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                        )
                    }

                if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer.start()
                }
            } else {
                mediaPlayer.start()
            }
        }
    }

    fun releaseMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            Handler().postDelayed({ releaseMediaPlayer() }, 1000)
        } else {
            mediaPlayer.release()
        }
    }
}
