package vn.sunasterisk.music_70.base

import android.os.Bundle
import androidx.annotation.LayoutRes

interface BaseView {

    val getContentViewId: Int

    fun initData(savedInstanceState: Bundle?)

    fun initComponents()
}
