package vn.sunasterisk.music_70.ui.discover

import vn.sunasterisk.music_70.data.model.Track
import java.lang.Exception

interface GenreContract {
    interface View {
        fun showTrack(tracks: List<Track>)

        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getTrack(api: String)
    }
}
