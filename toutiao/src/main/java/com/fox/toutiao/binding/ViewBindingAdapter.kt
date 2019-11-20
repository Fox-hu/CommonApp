package com.fox.toutiao.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.silver.fox.ext.orTrue

@BindingAdapter("android:onTabSelected", requireAll = false)
fun setOnNavigationItemSelectedListener(
    bnv: BottomNavigationView,
    itemSelected: ((Int) -> Boolean)?
) {
    bnv.setOnNavigationItemSelectedListener {
        itemSelected?.invoke(it.itemId).orTrue()
    }
}

@BindingAdapter("android:onNavSelected", requireAll = false)
fun setOnDrawNavigationItemListener(
    bnv: NavigationView,
    navSelected: ((Int) -> Boolean)?
) {
    bnv.setNavigationItemSelectedListener {
        navSelected?.invoke(it.itemId).orTrue()
    }
}