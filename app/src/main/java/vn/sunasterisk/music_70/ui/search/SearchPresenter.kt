package vn.sunasterisk.music_70.ui.search

import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackDataSource
import vn.sunasterisk.music_70.data.remote.TrackRepository
import vn.sunasterisk.music_70.util.StringUtils

class SearchPresenter(
    private val view: SearchContract.View,
    private val repository: TrackRepository
) : SearchContract.Presenter {
    override fun searchTracks(query: String) {
        val api = StringUtils.generateSearchUrl(query)
        repository.searchRemoteTracks(api, object : TrackDataSource.TrackCallback<List<Track>> {
            override fun onLoadTracksFail(error: Exception) {
                view.searchFail(error.toString())
            }

            override fun onLoadTracksSuccess(data: List<Track>) {
                view.searchSuccess(data)
            }

        })
    }
}
