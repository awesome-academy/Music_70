package vn.sunasterisk.music_70.data.remote

import vn.sunasterisk.music_70.data.model.Track

class TrackRepository(
    val remote: TrackDataSource.Remote,
    val local: TrackDataSource.Local
) : TrackDataSource.Local, TrackDataSource.Remote {

    override fun getRemoteTracks(api: String, callback: TrackDataSource.TrackCallback<List<Track>>) {
        remote.getRemoteTracks(api, callback)
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
