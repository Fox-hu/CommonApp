package com.fox.toutiao.ui.home


import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.logi

class HomeViewModel : BaseViewModel() {
    val itemId = MutableLiveData<Int>()
    val onNavSelected: (MenuItem) -> Boolean = { item ->
        itemId.value = item.itemId
        true
    }

    override fun onResume() {
        super.onResume()
        System.currentTimeMillis().toString().logi("currentTimeMillis")
    }
}