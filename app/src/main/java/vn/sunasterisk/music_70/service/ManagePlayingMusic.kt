package vn.sunasterisk.music_70.service

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import vn.sunasterisk.music_70.base.MyApplication
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.*
import java.io.IOException
import java.lang.RuntimeException

class ManagePlayingMusic : MediaPlayerListener {

    @StateType
    var state: Int = StateType.PAUSE
    @ShuffleType
    var shuffleType = ShuffleType.NO
    @LoopType
    var loopType = LoopType.NO

    private val mediaPlayer by lazy {
        MediaPlayer()
    }

    override fun createMediaPlayer(track: Track, listener: HandlerListenerPlayMusic) {
        mediaPlayer.reset()
        setContentType()
        try {
            if (track.isOnline) {
                mediaPlayer.setDataSource(
                    MyApplication.applicationContext,
                    Uri.parse(track.streamUrl)
                )
            } else {
                //description is path file
                mediaPlayer.setDataSource(track.description)
            }
            mediaPlayer.apply {
                setOnErrorListener(listener)
                setOnCompletionListener(listener)
                setOnPreparedListener(listener)
                prepareAsync()
            }
        } catch (e: IOException) {
            throw  RuntimeException("${track.title} can't play", e)
        }
    }

    private fun setContentType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = AudioAttributes.Builder()
            builder.setUsage(AudioAttributes.USAGE_MEDIA)
            builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            val attributes = builder.build()
            mediaPlayer.setAudioAttributes(attributes)
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    override fun start() {
        mediaPlayer.start()
        state = StateType.PLAY
    }

    override fun pause() {
        state = StateType.PAUSE
        mediaPlayer.pause()
    }

    override fun stop() {
        state = StateType.PAUSE
        mediaPlayer.stop()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun seek(position: Int) = mediaPlayer.seekTo(position)

    override fun getDuration() = mediaPlayer.duration

    override fun getCurrentPosition() =
        if (mediaPlayer.isPlaying) mediaPlayer.currentPosition else 0

    fun changeTrack(track: Track, mediaService: MediaService) {
        createMediaPlayer(track, mediaService)
    }

    fun setLooping() {
        mediaPlayer.isLooping = true
    }

    companion object {
        @Volatile
        private var INSTANCE: ManagePlayingMusic? = null

        fun getInstance() =
            INSTANCE ?: ManagePlayingMusic().also { INSTANCE = it }
    }
}
