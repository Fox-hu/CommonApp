package com.fox.toutiao.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.silver.fox.ext.orTrue

@BindingAdapter("android:bind_bn_onItemSelected", requireAll = false)
fun setOnNavigationItemSelectedListener(
    bnv: BottomNavigationView,
    itemSelected: ((Int) -> Boolean)?
) {
    bnv.setOnNavigationItemSelectedListener {
        itemSelected?.invoke(it.itemId).orTrue()
    }
}