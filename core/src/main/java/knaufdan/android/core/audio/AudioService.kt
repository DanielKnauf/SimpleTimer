package knaufdan.android.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import knaufdan.android.core.ContextProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioService @Inject constructor(private val contextProvider: ContextProvider) :
    IAudioService {

    private val players = mutableMapOf<Int, MediaPlayer>()

    private val audioManager by lazy {
        contextProvider.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun play(soundFileRes: Int) {
        val mediaPlayer = players.getOrPut(soundFileRes) {
            MediaPlayer.create(
                contextProvider.context,
                soundFileRes
            ).apply {
                setAudioAttributes(audioAttributes)
                setOnCompletionListener { releaseAudioFocus() }
            }
        }

        if (mediaPlayer.isPlaying) {
            return
        }

        val audioFocusChangeListener = mediaPlayer.createAudioFocusChangeListener()

        with(audioManager) {
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

    override fun release(soundFileRes: Int) {
        players[soundFileRes]?.apply {
            if (isPlaying) {
                Handler().postDelayed({ release(soundFileRes) }, 1000)
            } else {
                release()
                players.remove(soundFileRes)
            }
        }
    }

    private fun MediaPlayer.releaseAudioFocus() {
        if (!isPlaying) {
            with(audioManager) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    abandonAudioFocusRequest(
                        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).run {
                            setAudioAttributes(audioAttributes)
                            build()
                        }
                    )
                } else {
                    abandonAudioFocus(createAudioFocusChangeListener())
                }
            }
        }
    }

    private fun MediaPlayer.createAudioFocusChangeListener() =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            // loss of audio focus is indicated by a negative value
            if (focusChange < 0) this.stop()
        }

    companion object {
        private val audioAttributes by lazy {
            AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_MEDIA)
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                build()
            }
        }
    }
}
