package knaufdan.android.simpletimerapp.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import knaufdan.android.simpletimerapp.R
import javax.inject.Inject

class AudioService @Inject constructor(private val contextProvider: ContextProvider) {

    private val audioManager by lazy {
        contextProvider.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val playbackAttributes by lazy {
        AudioAttributes.Builder().run {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            build()
        }
    }

    private val audioFocusChangeListener by lazy {
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.d(
                    AudioService::class.simpleName,
                    "Gain audio focus"
                )
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                Log.d(
                    AudioService::class.simpleName,
                    "Loss audio focus"
                )
            }
        }
    }

    private val mediaPlayer by lazy {
        MediaPlayer.create(
            contextProvider.context,
            R.raw.gong_sound
        ).apply {
            setAudioAttributes(playbackAttributes)
            setOnCompletionListener { releaseAudioFocus() }
        }
    }

    private fun MediaPlayer.releaseAudioFocus() {
        if (!isPlaying) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(
                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).run {
                        setAudioAttributes(playbackAttributes)
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
                                setAudioAttributes(playbackAttributes)
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
}