package com.fox.toutiao.ui.home

import com.silver.fox.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val onItemSelected: (Int) -> Boolean = { itemId ->
//        var targetPosition = when (itemId) {
//            R.id.action_news -> showFragment(MainActivity.POSITION_NEWS)
//            else -> showFragment(MainActivity.POSITION_VIDEO)
//        }
        true
    }
}