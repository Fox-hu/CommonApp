package com.fox.toutiao

import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fox.annotation.Factory
import com.fox.annotation.view.BindView
import com.fox.toutiao.databinding.ActivityMainBinding
import com.fox.toutiao.ui.home.HomeViewModel
import com.silver.fox.activity.ViewTestActivity
import com.silver.fox.base.component.ui.BaseVMActivity
import com.silver.fox.ext.getString
import com.silver.fox.toSnackbarMsg
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.absoluteValue

class MainActivity : BaseVMActivity<HomeViewModel, ActivityMainBinding>() {
    private var lastBackPressMs = 0L
    private lateinit var appBarConfiguration: AppBarConfiguration

    override val viewModel: HomeViewModel by viewModel()

    @BindView(R.id.drawer_layout)
    var drawerLayout:DrawerLayout? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun bindDataAndEvent() {
        setSupportActionBar(toolbar)
        val host =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment? ?: return

        val navController = host.navController

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_news, R.id.home_photo, R.id.home_video, R.id.home_media),
            drawer_layout
        )

        //设置navigation 的 toolbar、navigationView、bottom_nav的跳转
//        nav_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_navigation.setupWithNavController(navController)

        viewModel.itemId.observe(this, Observer {
            toast("" + it)
            drawer_layout.closeDrawer(GravityCompat.START)
            when (it) {
                R.id.nav_camera -> startActivity<ViewTestActivity>()
                //findNavController().navigate(R.id.flow_step_one_dest, null, option)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.container).navigateUp(appBarConfiguration)
    }

    override fun getLayoutResId() = R.layout.activity_main

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val currentTimeMillis = System.currentTimeMillis()
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