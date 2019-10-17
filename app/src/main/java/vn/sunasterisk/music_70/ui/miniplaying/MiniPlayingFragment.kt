package vn.sunasterisk.music_70.ui.miniplaying

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import kotlinx.android.synthetic.main.fragment_mini_playing.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseFragment
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.service.MediaService
import vn.sunasterisk.music_70.service.PlayingMusicListener
import vn.sunasterisk.music_70.ui.nowplaying.NowPlayingActivity
import vn.sunasterisk.music_70.util.LoadImage
import vn.sunasterisk.music_70.util.StateType

class MiniPlayingFragment : BaseFragment(), PlayingMusicListener, View.OnClickListener {

    private var state = 0
    private lateinit var mediaService: MediaService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaService.BinderService
            mediaService = binder.getService().apply {
                setOnListenerMusic(this@MiniPlayingFragment)
            }
            updateUI(mediaService.getCurrentTrack())
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    override val getContentViewId = R.layout.fragment_mini_playing

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.bindService(
            context?.let { MediaService.getService(it) },
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun initComponents() {
    }

    fun updateUI(track: Track) {
        textNameSong.text = track.title
        textArtirst.text = track.artist
        track.artworkUrl?.let { LoadImage.loadImageCircleCrop(imageArtwork, it) }
    }

    override fun onPlayingStateListener(state: Int) {
        this.state = state
        if (state == StateType.PAUSE) {
            imagePlay.setImageResource(R.drawable.ic_play)
        } else {
            imagePlay.setImageResource(R.drawable.ic_pause)
        }
        updateUI(mediaService.getCurrentTrack())
    }

    override fun onTrackChangedListener(track: Track) {
        updateUI(track)
    }

    override fun registerListeners() {
        imageNext.setOnClickListener(this)
        imagePlay.setOnClickListener(this)
        imagePrevious.setOnClickListener(this)
        imageArtwork.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.imageNext -> mediaService.nextTrack()

            R.id.imagePrevious -> mediaService.previousTrack()

            R.id.imagePlay -> mediaService.playOrPauseTrack()

            else -> startActivity(
                NowPlayingActivity.getNowIntent(requireContext())
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    override fun unregisterListeners() {
    }

    companion object {
        fun newInstance() = MiniPlayingFragment()
    }
}
