package com.fox.toutiao.ui.home


import androidx.lifecycle.MutableLiveData
import com.fox.toutiao.R
import com.silver.fox.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val tabPosition = MutableLiveData<Int>()

    val onTabSelected: (Int) -> Boolean = { itemId ->
        val targetPosition = when (itemId) {
            R.id.action_news -> 0
            R.id.action_photo -> 1
            R.id.action_video -> 2
            R.id.action_media -> 3
            else -> -1
        }
        tabPosition.value = targetPosition
        true
    }

    val drawerPosition = MutableLiveData<Int>()

    val onNavSelected: (Int) -> Boolean = { itemId ->
        val targetPosition = when (itemId) {
            R.id.nav_camera -> 0
            R.id.nav_gallery -> 1
            R.id.nav_slideshow -> 2
            R.id.nav_manage -> 3
            R.id.nav_share -> 4
            R.id.nav_send -> 5
            else -> -1
        }
        drawerPosition.value = targetPosition
        true
    }
}