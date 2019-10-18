package vn.sunasterisk.music_70.ui.search

import vn.sunasterisk.music_70.data.model.Track

interface SearchContract {
    interface View {
        fun searchSuccess(tracks: List<Track>)
        fun searchFail(error: String)
    }

    interface Presenter {
        fun searchTracks(query: String)
    }
}
