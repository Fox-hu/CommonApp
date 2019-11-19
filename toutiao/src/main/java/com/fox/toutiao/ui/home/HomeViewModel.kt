package com.fox.toutiao.ui.home


import androidx.lifecycle.MutableLiveData
import com.fox.toutiao.R
import com.fox.toutiao.constants.TAB_NEWS
import com.fox.toutiao.constants.TAB_VIDEO
import com.silver.fox.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val tabPosition = MutableLiveData<Int>()

    val onItemSelected: (Int) -> Boolean = { itemId ->
        var targetPosition = when (itemId) {
            R.id.action_news -> tabPosition.postValue(TAB_NEWS)
            else -> tabPosition.postValue(TAB_VIDEO)
        }
        true
    }
}