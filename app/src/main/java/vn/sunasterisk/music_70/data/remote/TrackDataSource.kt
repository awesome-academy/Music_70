package vn.sunasterisk.music_70.data.remote

import vn.sunasterisk.music_70.data.model.Track
import java.lang.Exception

interface TrackDataSource {
    interface TrackCallback<T>{
        fun onLoadTracksSuccess(data: T)
        fun onLoadTracksFail(error: Exception)
    }

    interface Local {
    }

    interface Remote {
        fun getRemoteTracks(api: String, callback: TrackCallback<List<Track>>)
    }
}
