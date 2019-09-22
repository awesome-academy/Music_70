package vn.sunasterisk.music_70.data.remote

import android.content.Context
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.constant.Constant
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track

class RemoteDataSource : TrackDataSource.Remote {
    override fun getRemoteGenres(context: Context): List<Genre> {
        return listOf(
            Genre(
                Constant.GENRES_ALL_MUSIC,
                context.getString(R.string.string_all_music),
                R.drawable.bg_all_music
            ),
            Genre(
                Constant.GENRES_ALL_AUDIO,
                context.getString(R.string.string_audio),
                R.drawable.bg_all_audio
            ),
            Genre(
                Constant.GENRES_AMBIENT,
                context.getString(R.string.string_ambient),
                R.drawable.bg_ambient
            ),
            Genre(
                Constant.GENRES_COUNTRY,
                context.getString(R.string.string_contry),
                R.drawable.bg_country
            ),
            Genre(
                Constant.GENRES_ROCK,
                context.getString(R.string.string_rock),
                R.drawable.bg_rock
            ),
            Genre(
                Constant.GENRES_CLASSICAL,
                context.getString(R.string.string_classical),
                R.drawable.bg_classical
            )
        )
    }

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
