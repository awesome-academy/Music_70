package vn.sunasterisk.music_70.ui.discover

import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackDataSource
import vn.sunasterisk.music_70.data.remote.TrackRepository

class GenrePresenter(val repository: TrackRepository, val view: GenreContract.View) : GenreContract.Presenter {
    override fun getTrack(api: String) {
        repository.getRemoteTracks(api, object : TrackDataSource.TrackCallback<List<Track>> {
            override fun onLoadTracksSuccess(data: List<Track>) {
                view.showTrack(data)
            }

            override fun onLoadTracksFail(exception: Exception) {
                view.showError(exception)
            }
        })
    }
}
