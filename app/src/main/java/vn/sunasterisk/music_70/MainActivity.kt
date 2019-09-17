package vn.sunasterisk.music_70

import android.content.Context
import android.os.Bundle
import vn.sunasterisk.music_70.base.BaseActivity
import android.content.Intent.EXTRA_USER
import android.content.Intent

class MainActivity : BaseActivity() {
    override val getContentViewId = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
