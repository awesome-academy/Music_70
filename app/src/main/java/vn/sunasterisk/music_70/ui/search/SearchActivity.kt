package vn.sunasterisk.music_70.ui.search

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseActivity
import vn.sunasterisk.music_70.data.local.LocalDataSource
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.RemoteDataSource
import vn.sunasterisk.music_70.data.remote.TrackRepository
import vn.sunasterisk.music_70.service.MediaService
import vn.sunasterisk.music_70.ui.nowplaying.NowPlayingActivity

class SearchActivity : BaseActivity(), SearchContract.View, SearchView.OnQueryTextListener,
    View.OnClickListener {

    private lateinit var searchPresenter: SearchPresenter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var tracksRepository: TrackRepository

    private lateinit var mediaService: MediaService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaService.BinderService
            mediaService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaService = MediaService()
        bindService(MediaService.getService(this), connection, Context.BIND_AUTO_CREATE)
    }

    override fun searchSuccess(tracks: List<Track>) {
        searchAdapter.submitList(tracks as MutableList<Track>)
    }

    override fun searchFail(error: String) {
    }

    override fun registerListeners() {
        searchView.apply {
            isActivated = true
            onActionViewExpanded()
            clearFocus()
            setOnQueryTextListener(this@SearchActivity)
        }
        imageBack.setOnClickListener(this)
    }

    override fun unregisterListeners() {
    }

    override val getContentViewId = R.layout.activity_search

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        tracksRepository = TrackRepository.getInstance(
            RemoteDataSource.getInstance(applicationContext),
            LocalDataSource.getInstance(applicationContext)
        )
        searchPresenter = SearchPresenter(this, tracksRepository)
        searchAdapter = SearchAdapter { tracks, index ->
            mediaService.addTracks(tracks)
            mediaService.setCurrentTrack(tracks[index])
            startActivity(NowPlayingActivity.getNowIntent(this))
        }
        recyclerTracks.adapter= searchAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty())
            Toast.makeText(this, getString(R.string.string_no_input), Toast.LENGTH_SHORT).show()
        else {
            Handler().postDelayed({
                searchPresenter.searchTracks(newText)
            }, TIME_DELAY)
        }
        return false
    }

    override fun onClick(view: View?) {
        when (view) {
            imageBack -> onBackPressed()
        }
    }

    companion object {
        const val TIME_DELAY = 300L
        fun getIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }
}
