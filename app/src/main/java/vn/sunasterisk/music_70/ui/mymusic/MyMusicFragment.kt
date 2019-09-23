package vn.sunasterisk.music_70.ui.mymusic

import android.os.Bundle
import vn.sunasterisk.music_70.R
import vn.sunasterisk.music_70.base.BaseFragment

class MyMusicFragment : BaseFragment(){

    override val getContentViewId = R.layout.fragment_my_music

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    companion object {
        fun newInstance() = MyMusicFragment()
    }
}
