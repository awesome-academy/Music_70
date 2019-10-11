package vn.sunasterisk.music_70

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import vn.sunasterisk.music_70.base.BaseActivity
import vn.sunasterisk.music_70.service.MediaService
import vn.sunasterisk.music_70.ui.chart.ChartFragment
import vn.sunasterisk.music_70.ui.discover.DiscoverFragment
import vn.sunasterisk.music_70.ui.miniplaying.MiniPlayingFragment
import vn.sunasterisk.music_70.ui.mymusic.MyMusicFragment
import vn.sunasterisk.music_70.ui.setting.SettingFragment
import vn.sunasterisk.music_70.ui.user.UserFragment

class MainActivity : BaseActivity() {

    private lateinit var mediaService: MediaService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaService.BinderService
            mediaService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaService = MediaService()
        bindService(MediaService.getService(this), connection, Context.BIND_AUTO_CREATE)
    }

    override val getContentViewId = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
        addFragment(supportFragmentManager, DiscoverFragment.newInstance(), true)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.menu_discover
    }

    override fun registerListeners() {
    }

    override fun unregisterListeners() {
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val newFragment = when (item.itemId) {
                R.id.menu_my_music -> MyMusicFragment.newInstance()
                R.id.menu_discover -> DiscoverFragment.newInstance()
                R.id.menu_chart -> ChartFragment.newInstance()
                R.id.menu_user -> UserFragment.newInstance()
                R.id.menu_setting -> SettingFragment.newInstance()
                else -> return@OnNavigationItemSelectedListener false
            }
            addFragment(supportFragmentManager, newFragment, false)
            return@OnNavigationItemSelectedListener true
        }

    fun addFragment(
        fragmentManager: FragmentManager, fragment: Fragment,
        isAddFragment: Boolean
    ) {
        val currentFragment = fragmentManager.findFragmentByTag(fragment::class.java.name)
        val transaction = fragmentManager.beginTransaction()
        currentFragment?.let {
            if (currentFragment.isVisible) return
            transaction.show(currentFragment).commit()
        }
        if (isAddFragment) {
            transaction.add(R.id.container, fragment, fragment::class.java.name)
        } else {
            transaction.replace(R.id.container, fragment, fragment::class.java.name)
        }
        transaction.commit()
    }

    fun initMiniPlaying() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val miniPlaying =
            supportFragmentManager.findFragmentByTag(MiniPlayingFragment::class.java.name) as? MiniPlayingFragment
        miniPlaying?.updateUI(mediaService.getCurrentTrack()) ?: run {
            fragmentTransaction.add(
                R.id.frameMiniPlaying,
                MiniPlayingFragment.newInstance(),
                MiniPlayingFragment::class.java.name
            ).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mediaService.getSizeTracks() > 0) initMiniPlaying()
    }

    fun getMediaService(): MediaService? = if (::mediaService.isInitialized) mediaService else null

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
