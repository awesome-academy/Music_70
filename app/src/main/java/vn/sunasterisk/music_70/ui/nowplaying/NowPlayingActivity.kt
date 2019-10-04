package vn.sunasterisk.music_70.ui.nowplaying

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_now_playing.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseActivity
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackAttributes
import vn.sunasterisk.music_70.service.MediaService
import vn.sunasterisk.music_70.service.PlayingMusicListener
import vn.sunasterisk.music_70.util.*
import java.util.*

class NowPlayingActivity : BaseActivity(),
    View.OnClickListener,
    PlayingMusicListener,
    SeekBar.OnSeekBarChangeListener {

    private var state = 0
    private lateinit var mediaService: MediaService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaService.BinderService

            mediaService = binder.getService().apply {
                getListTrack()?.let { addTracks(it) }
                setOnListenerMusic(this@NowPlayingActivity)
                getTrackReceived()?.let { setCurrentTrack(it) }
                getTrackReceived()?.let { playMusic(it) }
            }
            getTrackReceived()?.let { updateUI(it) }
            updateCurrentTime()
            rotateTheDisk()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    private fun getListTrack() = intent?.getParcelableArrayListExtra<Track>(TrackAttributes.TRACK)

    private fun getTrackReceived() = getPositionTrack()?.let { getListTrack()?.get(it) }

    private fun getPositionTrack() = intent?.getIntExtra(POSITION_TRACK, 0)

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
        seekSongProcess.setOnSeekBarChangeListener(this)
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
        textSongDuration.text = track.duration.let { StringUtils.convertTime(it) }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonPlay -> if (::mediaService.isInitialized) mediaService.playOrPauseTrack()

            R.id.buttonPrevious -> {
                if (::mediaService.isInitialized) mediaService.previousTrack()
                updateUI(mediaService.getCurrentTrack())
            }

            R.id.buttonNext -> {
                if (::mediaService.isInitialized) mediaService.nextTrack()
                updateUI(mediaService.getCurrentTrack())
            }

            R.id.buttonShuffe -> handleShuffle()

            R.id.buttonLoop -> handleLoop()

            R.id.buttonFavourite -> {
            }

            R.id.buttonBack -> onBackPressed()

            R.id.buttonOption -> {
                BottomSheetFragment
                    .newInstance(mediaService.getCurrentTrack())
                    .show(supportFragmentManager, BottomSheetFragment::class.java.name)
            }
        }
    }

    private fun updateCurrentTime() {
        seekSongProcess.max = getTrackReceived()!!.duration / TIME_SECOND
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

    private fun handleShuffle() {
        when (mediaService.shuffleType) {
            ShuffleType.NO -> {
                mediaService.shuffleType = ShuffleType.YES
                buttonShuffe.setColorFilter(ActivityCompat.getColor(this, R.color.color_accent))
            }
            ShuffleType.YES -> {
                mediaService.shuffleType = ShuffleType.NO
                buttonShuffe.setColorFilter(ActivityCompat.getColor(this, R.color.color_gray))
            }
        }
    }

    private fun handleLoop() {
        when (mediaService.loopType) {
            LoopType.NO -> {
                mediaService.loopType = LoopType.ALL
                buttonLoop.setColorFilter(ActivityCompat.getColor(this, R.color.color_accent))
            }
            LoopType.ALL -> {
                mediaService.loopType = LoopType.ONE
                buttonLoop.setImageResource(R.drawable.ic_loop_one)
            }
            LoopType.ONE -> {
                mediaService.loopType = LoopType.NO
                buttonLoop.setImageResource(R.drawable.ic_loop)
                buttonLoop.setColorFilter(ActivityCompat.getColor(this, R.color.color_gray))
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) mediaService.seekMusic(progress * TIME_SECOND)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
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
