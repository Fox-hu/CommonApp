package com.fox.toutiao

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.fox.toutiao.databinding.ActivityMainBinding
import com.fox.toutiao.ui.ToolbarManager
import com.fox.toutiao.ui.home.HomeViewModel
import com.fox.toutiao.ui.news.NewsFragment
import com.google.android.material.navigation.NavigationView
import com.silver.fox.base.BaseVMActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseVMActivity<HomeViewModel, ActivityMainBinding>(), ToolbarManager,
    NavigationView.OnNavigationItemSelectedListener {

    override val viewModel: HomeViewModel by viewModel()

    companion object {
        const val POSITION_NEWS = 0
        const val POSITION_VIDEO = 1
    }

    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    private val titleList = arrayOf("新闻", "图片", "视频", "头条号")
    private var newsFragment: NewsFragment? = null
    private var videoFragment: NewsFragment? = null
    private var index: Int = 0


    override fun startObserver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        toolbar.inflateMenu(R.menu.menu_activity_main)
        setSupportActionBar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener { handleBottomNavClick(it) }

        var toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//
//        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleBottomNavClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_news -> showFragment(POSITION_NEWS)
            else -> showFragment(POSITION_VIDEO)
        }
        return true
    }

    private fun showFragment(position: Int) {
        var ft = supportFragmentManager.beginTransaction()
        hideFragment(ft)
//        index = position
//        val value: Any = when (position) {
//            POSITION_NEWS -> newsFragment?.let {
//                ft.show(it)
//            } ?: {
//                newsFragment = NewsFragment.get()
//                ft.add(R.id.container, newsFragment!!, NewsFragment::class.java.simpleName)
//            }
//        }
    }

    private fun hideFragment(ft: FragmentTransaction) {

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}