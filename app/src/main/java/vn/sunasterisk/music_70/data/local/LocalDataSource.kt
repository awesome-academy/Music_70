package vn.sunasterisk.music_70.data.local

import android.content.Context
import vn.sunasterisk.music_70.data.remote.TrackDataSource

class LocalDataSource : TrackDataSource.Local {
    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LocalDataSource().also { INSTANCE = it }
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }
}
