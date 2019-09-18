package vn.sunasterisk.music_70.data.remote

import android.content.Context
import vn.sunasterisk.music_70.data.model.Track

class RemoteDataSource : TrackDataSource.Remote {
    override fun getRemoteTracks(api: String, callback: TrackDataSource.TrackCallback<List<Track>>) {
        TrackAsyncTask(callback).execute(api)
    }

    companion object {
        @Volatile
        private var INSTANCE: RemoteDataSource? = null

        @JvmStatic
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: RemoteDataSource().also { INSTANCE = it }
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }
}
