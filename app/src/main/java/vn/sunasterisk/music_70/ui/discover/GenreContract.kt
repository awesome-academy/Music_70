package vn.sunasterisk.music_70.ui.discover

import android.content.Context
import vn.sunasterisk.music_70.data.model.Genre
import vn.sunasterisk.music_70.data.model.Track
import java.lang.Exception

interface GenreContract {
    interface View {
        fun showTrack(tracks: List<Track>, isTrending: Boolean)

        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getTrack(api: String, isTrending: Boolean)
        fun getListGenre(context: Context): List<Genre>
    }
}
