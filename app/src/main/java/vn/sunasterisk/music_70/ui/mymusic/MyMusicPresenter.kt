package vn.sunasterisk.music_70.ui.mymusic

import vn.sunasterisk.music_70.data.model.Track
import vn.sunasterisk.music_70.util.MusicExternalManager

class MyMusicPresenter(val manager: MusicExternalManager) : MyMusicContract.Presenter {
    override fun getAllMusic(): List<Track> = manager.externals
}
