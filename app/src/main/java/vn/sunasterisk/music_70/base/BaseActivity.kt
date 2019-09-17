package vn.sunasterisk.music_70.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId)
        initData(savedInstanceState)
        initComponents()
        registerListeners()
    }

    override fun onDestroy() {
        unregisterListeners()
        super.onDestroy()
    }

    abstract fun registerListeners()

    abstract fun unregisterListeners()
}
