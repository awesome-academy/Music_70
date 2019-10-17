package vn.sunasterisk.music_70.base

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: MyApplication
            private set
        val applicationContext: Context?
            get() = if (::INSTANCE.isInitialized) INSTANCE.applicationContext else null
    }
}
