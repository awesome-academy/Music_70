package vn.sunasterisk.music_70

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import vn.sunasterisk.music_70.base.BaseActivity
import vn.sunasterisk.music_70.constant.Constant
import vn.sunasterisk.music_70.ui.chart.ChartFragment
import vn.sunasterisk.music_70.ui.discover.DiscoverFragment
import vn.sunasterisk.music_70.ui.mymusic.MyMusicFragment
import vn.sunasterisk.music_70.ui.setting.SettingFragment
import vn.sunasterisk.music_70.ui.user.UserFragment

class MainActivity : BaseActivity() {
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

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item?.itemId) {
            R.id.menu_my_music -> {
                addFragment(supportFragmentManager, MyMusicFragment.newInstance(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_discover -> {
                addFragment(supportFragmentManager, DiscoverFragment.newInstance(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_chart -> {
                addFragment(supportFragmentManager, ChartFragment.newInstance(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_user -> {
                addFragment(supportFragmentManager, UserFragment.newInstance(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_setting -> {
                addFragment(
                    supportFragmentManager, SettingFragment.newInstance(), false
                )
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun addFragment(
        fragmentManager: FragmentManager, fragment: Fragment,
        isAddFragment: Boolean
    ) {
        val currentFragment = fragmentManager.findFragmentByTag(fragment::class.java.name) as Fragment?
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

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
