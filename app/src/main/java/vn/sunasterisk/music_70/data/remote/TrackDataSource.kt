package vn.sunasterisk.music_70.data.remote

import android.content.Context
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track
import java.lang.Exception

interface TrackDataSource {
    interface TrackCallback<T> {
        fun onLoadTracksSuccess(data: T)
        fun onLoadTracksFail(error: Exception)
    }

    interface Local {
    }

    interface Remote {
        fun getRemoteTracks(api: String, callback: TrackCallback<List<Track>>)
        fun getRemoteGenres(context: Context): List<Genre>
        fun searchRemoteTracks(api: String, callback: TrackCallback<List<Track>>)
    }
}
