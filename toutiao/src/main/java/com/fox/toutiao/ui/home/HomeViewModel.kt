package com.fox.toutiao.ui.home


import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.silver.fox.base.BaseViewModel

class HomeViewModel : BaseViewModel() {
    val itemId = MutableLiveData<Int>()
    val onNavSelected: (MenuItem) -> Boolean = { item ->
        itemId.value = item.itemId
        true
    }
}