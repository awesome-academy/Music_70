package vn.sunasterisk.music_70.ui.mymusic

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_my_music.*
import vn.sunasterisk.music_70.MainActivity
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseFragment
import vn.sunasterisk.music_70.ui.discover.ListSongAdapter
import vn.sunasterisk.music_70.ui.nowplaying.NowPlayingActivity
import vn.sunasterisk.music_70.util.MusicExternalManager
import vn.sunasterisk.music_70.util.PermissionUtil

class MyMusicFragment : BaseFragment() {

    private val presenter by lazy {
        context?.let {
            MyMusicPresenter(MusicExternalManager(it))
        }
    }

    override val getContentViewId = R.layout.fragment_my_music

    override fun initData(savedInstanceState: Bundle?) {
        if (isStoragePermissionGranted()) {
            initAdapter()
            return
        }
        PermissionUtil.requestStoragePermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_STORAGE -> {
                if (PermissionUtil.isGrantResultsGranted(grantResults)) {
                    initAdapter()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.alert_permissions_rationale),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initAdapter() {
        attachAdapterToRecyclerView(
            recycleMyMusic, ListSongAdapter({ tracks, index ->
                (activity as MainActivity).getMediaService()?.let { mediaService ->
                    mediaService.addTracks(tracks)
                    mediaService.setCurrentTrack(tracks[index])
                }
                startActivity(NowPlayingActivity.getNowIntent(requireContext()))
            }, {})
        )
    }

    private fun attachAdapterToRecyclerView(
        recyclerView: RecyclerView,
        deviceAdapter: ListSongAdapter
    ) {
        presenter?.let { deviceAdapter.updateList(it.getAllMusic()) }
        recyclerView.adapter = deviceAdapter
    }

    private fun isStoragePermissionGranted(): Boolean {
        return PermissionUtil.isPermissionsGranted(context!!, PermissionUtil.storagePermissions)
    }

    override fun initComponents() {
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    companion object {
        fun newInstance() = MyMusicFragment()
        const val PERMISSION_STORAGE = 1111
    }
}
