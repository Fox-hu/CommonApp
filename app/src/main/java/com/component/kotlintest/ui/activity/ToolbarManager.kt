package com.component.kotlintest.ui.activity


import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.component.kotlintest.App
import com.component.kotlintest.R
import com.component.kotlintest.extensions.ctx
import com.component.kotlintest.extensions.sildeEnter
import com.component.kotlintest.extensions.sildeExit
import com.component.kotlintest.fragment.FragmentActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

interface ToolbarManager {
    //接口中的toolbar变量
    val toolbar: Toolbar

    //接口中的String变量 已经设置好set get方法
    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value) {
            toolbar.title = value
        }

    fun initToolbar() {
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> toolbar.ctx.startActivity<SettingActivity>()
                R.id.fragment -> toolbar.ctx.startActivity<FragmentActivity>()
                else -> App.instance.toast("Unknown option")
            }
            true
        }
    }

    fun enableHomeAsUp(up: () -> Unit) {
        toolbar.navigationIcon = createUpDrawable()
        toolbar.setNavigationOnClickListener { up() }
    }

    fun createUpDrawable() = DrawerArrowDrawable(toolbar.ctx).apply { progress = 1f }

    fun attachToScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) toolbar.sildeExit() else toolbar.sildeEnter()
            }
        })
    }
}