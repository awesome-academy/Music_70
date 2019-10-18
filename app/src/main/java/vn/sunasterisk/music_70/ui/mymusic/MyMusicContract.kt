package vn.sunasterisk.music_70.ui.mymusic

import vn.sunasterisk.music_70.data.model.Track

interface MyMusicContract {
    interface View

    interface Presenter {
        fun getAllMusic(): List<Track>
    }
}
