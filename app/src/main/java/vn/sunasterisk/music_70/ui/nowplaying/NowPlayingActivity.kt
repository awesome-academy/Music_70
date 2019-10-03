package vn.sunasterisk.music_70.ui.nowplaying

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_now_playing.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseActivity
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.service.MediaService
import vn.sunasterisk.music_70.service.PlayingMusicListener
import vn.sunasterisk.music_70.util.LoadImage
import vn.sunasterisk.music_70.util.StateType
import vn.sunasterisk.music_70.util.StringUtils
import java.util.ArrayList

class NowPlayingActivity : BaseActivity(), View.OnClickListener, PlayingMusicListener {

    private var state = 0
    private lateinit var mediaService: MediaService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaService.BinderService
            mediaService = binder.getService()
            mediaService.setOnListenerMusic(this@NowPlayingActivity)
            mediaService.setCurrentTrack(getTrackReceived())
            mediaService.playMusic(getTrackReceived())
            updateUI(getTrackReceived())
            updateCurrentTime()
            rotateTheDisk()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    private fun getTrackReceived() =
        intent.getParcelableArrayListExtra<Track>(TrackAttributes.TRACK)[getPositionTrack()]

    private fun getPositionTrack() = intent.getIntExtra(POSITION_TRACK, 0)

    override fun onPlayingStateListener(state: Int) {
        this.state = state
        if (state == StateType.PAUSE) {
            buttonPlay.setImageResource(R.drawable.ic_play)
        } else {
            buttonPlay.setImageResource(R.drawable.ic_pause)
            rotateTheDisk()
        }
    }

    override fun onTrackChangedListener(track: Track) {
    }

    override fun registerListeners() {
        buttonPlay.setOnClickListener(this)
        buttonBack.setOnClickListener(this)
        buttonFavourite.setOnClickListener(this)
        buttonNext.setOnClickListener(this)
        buttonPrevious.setOnClickListener(this)
        buttonLoop.setOnClickListener(this)
        buttonShuffe.setOnClickListener(this)
    }

    override fun unregisterListeners() {
        unbindService(connection)
    }

    override val getContentViewId = R.layout.activity_now_playing

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService(getMediaService(this), connection, Context.BIND_AUTO_CREATE)
    }

    fun updateUI(track: Track) {
        textSongName.text = track.title
        textArtist.text = track.artist
        track.artworkUrl?.let { LoadImage.loadImageCircleCrop(imageSong, it) }
        textSongDuration.text =
            mediaService.getDuration().let { StringUtils.convertTime(it) }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonPlay -> {
                mediaService.playOrPauseTrack()
            }

            R.id.buttonPrevious -> {
            }

            R.id.buttonNext -> {
            }

            R.id.buttonShuffe -> {
            }

            R.id.buttonLoop -> {
            }

            R.id.buttonFavourite -> {
            }

            R.id.buttonBack -> {
                onBackPressed()
            }

            R.id.buttonOption -> {
            }
        }
    }

    private fun updateCurrentTime() {
        seekSongProcess.max = mediaService.getDuration() / TIME_SECOND
        val handlerSyncTime = Handler()
        handlerSyncTime.postDelayed(object : Runnable {
            override fun run() {
                if (state == StateType.PLAY) {
                    val currentTime = mediaService.getCurrentPosition() / TIME_SECOND
                    seekSongProcess.progress = currentTime
                    textSongCurrent.text =
                        StringUtils.convertTime(mediaService.getCurrentPosition())
                }
                handlerSyncTime.postDelayed(this, TIME_DELAY)
            }
        }, TIME_DELAY)
    }

    private fun rotateTheDisk() {
        if (state == StateType.PAUSE) return
        imageSong
            .animate()
            .setDuration(10)
            .rotation(imageSong.rotation + 2f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    rotateTheDisk()
                    super.onAnimationEnd(animation)
                }
            })
    }

    companion object {
        const val TIME_SECOND = 1000
        const val TIME_DELAY = 1000L
        const val POSITION_TRACK = "POSITION_TRACK"
        fun getMediaService(context: Context) = Intent(context, MediaService::class.java)
        fun getIntent(context: Context, track: List<Track>, position: Int) =
            Intent(context, NowPlayingActivity::class.java).apply {
                putExtra(POSITION_TRACK, position)
                putParcelableArrayListExtra(
                    TrackAttributes.TRACK,
                    track as ArrayList<out Parcelable>
                )
            }
    }
}
