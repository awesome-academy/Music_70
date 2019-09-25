package vn.sunasterisk.music_70.ui.discover

import android.content.Context
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.data.remote.TrackDataSource
import vn.sunasterisk.music_70.data.remote.TrackRepository

class GenrePresenter(private val repository: TrackRepository, private val view: GenreContract.View) :
    GenreContract.Presenter {

    override fun getTrack(api: String, isTrending: Boolean) {
        repository.getRemoteTracks(api, object : TrackDataSource.TrackCallback<List<Track>> {
            override fun onLoadTracksSuccess(data: List<Track>) {
                view.showTrack(data, isTrending)
            }

            override fun onLoadTracksFail(exception: Exception) {
                view.showError(exception)
            }
        })
    }

    override fun getListGenre(context: Context): List<Genre> = repository.getRemoteGenres(context)
}
