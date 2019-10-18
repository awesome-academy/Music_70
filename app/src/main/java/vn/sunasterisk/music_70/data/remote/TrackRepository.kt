package vn.sunasterisk.music_70.data.remote

import android.content.Context
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track

class TrackRepository(
    val remote: TrackDataSource.Remote,
    val local: TrackDataSource.Local
) : TrackDataSource.Local, TrackDataSource.Remote {
    override fun searchRemoteTracks(
        api: String,
        callback: TrackDataSource.TrackCallback<List<Track>>
    ) {
        remote.searchRemoteTracks(api, callback)
    }

    override fun getRemoteTracks(
        api: String,
        callback: TrackDataSource.TrackCallback<List<Track>>
    ) {
        remote.getRemoteTracks(api, callback)
    }

    override fun getRemoteGenres(context: Context): List<Genre> {
        return remote.getRemoteGenres(context)
    }

    companion object {
        private var INSTANCE: TrackRepository? = null
        @JvmStatic
        fun getInstance(
            remote: TrackDataSource.Remote,
            local: TrackDataSource.Local
        ): TrackRepository = INSTANCE ?: TrackRepository(remote, local).apply { INSTANCE = this }
    }
}

