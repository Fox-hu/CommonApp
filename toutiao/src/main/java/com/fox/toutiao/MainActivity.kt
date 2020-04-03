package com.fox.toutiao

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.fox.toutiao.databinding.ActivityMainBinding
import com.fox.toutiao.ui.home.HomeViewModel
import com.silver.fox.activity.SlideActivity
import com.silver.fox.activity.TestActivity
import com.silver.fox.activity.VerticalDragActivity
import com.silver.fox.base.BaseVMActivity
import com.silver.fox.ext.getString
import com.silver.fox.ext.startKtxActivity
import com.silver.fox.activity.toSnackbarMsg
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.absoluteValue

class MainActivity : BaseVMActivity<HomeViewModel, ActivityMainBinding>() {

    override val viewModel: HomeViewModel by viewModel()

    private var lastBackPressMs = 0L

    //    override val toolbar: Toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    private val titleList = arrayOf("新闻", "图片", "视频", "头条号")
    //    private var newsFragment: NewsFragment? = null
//    private var videoFragment: NewsFragment? = null
    private var index: Int = 0

    override fun startObserver() {
        viewModel.drawerPosition.observe(this, Observer {
            toast("" + it)
            drawer_layout.closeDrawer(GravityCompat.START)
            when (it) {
                0 -> startKtxActivity<SlideActivity>()
                1 -> startKtxActivity<TestActivity>()
                2 -> startKtxActivity<VerticalDragActivity>()
            }
        })
    }

    override fun initData() {

    }

    override fun initView() {
        toolbar.inflateMenu(R.menu.menu_activity_main)
        setSupportActionBar(toolbar)

        var toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
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

    override fun getLayoutResId() = R.layout.activity_main

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            var currentTimeMillis = System.currentTimeMillis()
            if ((currentTimeMillis - lastBackPressMs).absoluteValue > 2000L) {
                viewModel.snackbarData.postValue(R.string.exit.getString().toSnackbarMsg())
                lastBackPressMs = currentTimeMillis
            } else {
                //这个是退到后台 进入onstop 并未finish
                moveTaskToBack(true)
            }
        }
    }
}