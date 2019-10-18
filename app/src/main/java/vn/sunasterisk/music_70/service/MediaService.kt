package vn.sunasterisk.music_70.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import vn.sunasterisk.music_70.BuildConfig
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.LoopType
import vn.sunasterisk.music_70.util.ShuffleType.Companion.NO
import vn.sunasterisk.music_70.util.StateType
import java.util.*

class MediaService : Service(), HandlerListenerPlayMusic {

    private val managerPlayingMusic by lazy {
        ManagePlayingMusic.getInstance()
    }
    private val playNotification by lazy { PlayNotification() }
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                ACTION_PLAY -> {
                    playOrPauseTrack()
                }
                ACTION_NEXT -> {
                    nextTrack()
                }
                ACTION_PREVIOUS -> {
                    previousTrack()
                }
                ACTION_QUIT -> {
                    playNotification.stop()
                    stopSelf()
                }
            }
        }
        return START_NOT_STICKY
    }

    fun setOnListenerMusic(playingMusicListener: PlayingMusicListener) {
        this.playingMusicListener = playingMusicListener
    }

    fun playMusic(track: Track) {
        managerPlayingMusic.createMediaPlayer(track, this)
        managerPlayingMusic.start()
        playNotification.init(this)
        playNotification.update(track, true)
    }

    fun playOrPauseTrack() {
        if (managerPlayingMusic.state == StateType.PAUSE) {
            managerPlayingMusic.start()
            playNotification.update(currentTrack, true)
            playingMusicListener.onPlayingStateListener(StateType.PLAY)
        } else {
            managerPlayingMusic.pause()
            playNotification.update(currentTrack, true)
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
            playNotification.update(getNextTrack(), true)
        } else {
            currentTrack = getRandomTrack()
            managerPlayingMusic.changeTrack(getRandomTrack(), this)
            addListener(getRandomTrack())
            playNotification.update(getRandomTrack(), true)
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
        private val PACKAGE_NAME = BuildConfig.APPLICATION_ID
        val ACTION_PLAY = "$PACKAGE_NAME.play"
        val ACTION_PREVIOUS = "$PACKAGE_NAME.previous"
        val ACTION_NEXT = "$PACKAGE_NAME.next"
        val ACTION_QUIT = "$PACKAGE_NAME.quitservice"
        fun getService(context: Context) = Intent(context, MediaService::class.java)
    }
}
