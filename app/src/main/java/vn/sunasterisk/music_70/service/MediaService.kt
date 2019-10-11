package vn.sunasterisk.music_70.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.LoopType
import vn.sunasterisk.music_70.util.ShuffleType.Companion.NO
import vn.sunasterisk.music_70.util.StateType
import java.util.*

class MediaService : Service(), HandlerListenerPlayMusic {

    private val managerPlayingMusic by lazy {
        ManagePlayingMusic.getInstance()
    }
    private var listTrack = emptyList<Track>()
    private lateinit var binder: BinderService
    private lateinit var currentTrack: Track
    private lateinit var playingMusicListener: PlayingMusicListener
    var idTrackPlaying = 0
    var shuffleType: Int
        get() = managerPlayingMusic.shuffleType
        set(value) {
            managerPlayingMusic.shuffleType = value
        }

    var loopType: Int
        get() = managerPlayingMusic.loopType
        set(value) {
            managerPlayingMusic.loopType = value
        }

    override fun onCreate() {
        super.onCreate()
        binder = BinderService()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onPrepared(mp: MediaPlayer?) {
        managerPlayingMusic.start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        when (loopType) {
            LoopType.ONE -> managerPlayingMusic.setLooping()
            LoopType.ALL -> nextTrack()
            else -> {
                if (isLastListTrack(currentTrack)) {
                    stopTrack()
                } else {
                    nextTrack()
                }
            }
        }
    }

    fun setOnListenerMusic(playingMusicListener: PlayingMusicListener) {
        this.playingMusicListener = playingMusicListener
    }

    fun playMusic(track: Track) {
        managerPlayingMusic.createMediaPlayer(track, this)
        managerPlayingMusic.start()
    }

    fun playOrPauseTrack() {
        if (managerPlayingMusic.state == StateType.PAUSE) {
            managerPlayingMusic.start()
            playingMusicListener.onPlayingStateListener(StateType.PLAY)
        } else {
            managerPlayingMusic.pause()
            playingMusicListener.onPlayingStateListener(StateType.PAUSE)
        }
    }

    fun getCurrentPosition() = managerPlayingMusic.getCurrentPosition()

    fun getDuration() = managerPlayingMusic.getDuration()

    fun addTracks(listTrack: List<Track>) {
        this.listTrack = listTrack
    }

    private fun addListener(track: Track) {
        playingMusicListener.onPlayingStateListener(StateType.PLAY)
        playingMusicListener.onTrackChangedListener(track)
    }

    fun isLastListTrack(current: Track): Boolean {
        return listTrack.indexOf(current) == listTrack.size - NUMBER_NEXT_SONG
    }

    fun stopTrack() = managerPlayingMusic.stop()

    fun previousTrack() {
        currentTrack = getPreviousTrack()
        managerPlayingMusic.changeTrack(getPreviousTrack(), this)
        addListener(getPreviousTrack())
    }

    private fun getPreviousTrack(): Track {
        val position = listTrack.indexOf(currentTrack)
        return if (position == ELEMENT_FIRST) {
            listTrack[listTrack.size - NUMBER_NEXT_SONG]
        } else listTrack[position - NUMBER_NEXT_SONG]
    }

    fun setCurrentTrack(currentTrack: Track) {
        this.currentTrack = currentTrack
    }

    private fun getNextTrack(): Track {
        val position = listTrack.indexOf(currentTrack)
        return if (position == listTrack.size - NUMBER_NEXT_SONG) {
            listTrack[ELEMENT_FIRST]
        } else listTrack[position + NUMBER_NEXT_SONG]
    }

    fun nextTrack() {
        if (shuffleType == NO) {
            currentTrack = getNextTrack()
            managerPlayingMusic.changeTrack(getNextTrack(), this)
            addListener(getNextTrack())
        } else {
            currentTrack = getRandomTrack()
            managerPlayingMusic.changeTrack(getRandomTrack(), this)
            addListener(getRandomTrack())
        }
    }

    private fun getRandomTrack(): Track {
        val random = Random()
        return listTrack[random.nextInt(listTrack.size)]
    }

    fun getCurrentTrack() = currentTrack

    fun seekMusic(duration: Int) {
        managerPlayingMusic.seek(duration)
    }

    fun getSizeTracks() = listTrack.size

    override fun onBind(intent: Intent) = binder

    inner class BinderService : Binder() {
        fun getService(): MediaService = this@MediaService
    }

    companion object {
        const val NUMBER_NEXT_SONG = 1
        const val ELEMENT_FIRST = 0
        fun getService(context: Context) = Intent(context, MediaService::class.java)
    }
}
