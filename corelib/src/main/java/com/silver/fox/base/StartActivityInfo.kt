package com.silver.fox.base

import android.content.Intent

/**
 * created by francis.fan on 2019/1/5
 * 用于包在SingleLiveData中，View监听到ViewModel中该LiveData改变时进行页面跳转
 */
class StartActivityInfo {
    var requestCode = NO_REQUEST_CODE
    val intent: Intent

    constructor(intent: Intent) {
        this.intent = intent
    }

    constructor(intent: Intent, requestCode: Int) {
        this.requestCode = requestCode
        this.intent = intent
    }

    companion object {
        const val NO_REQUEST_CODE = -1
    }
}