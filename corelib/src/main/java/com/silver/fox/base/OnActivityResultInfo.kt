package com.silver.fox.base

import android.os.Bundle

/**
 * created by francis.fan on 2019/1/7
 */
class OnActivityResultInfo {
    val resultCode: Int
    var resultBundle: Bundle? = null
        private set

    constructor(resultCode: Int) {
        this.resultCode = resultCode
    }

    constructor(resultCode: Int, resultBundle: Bundle?) {
        this.resultCode = resultCode
        this.resultBundle = resultBundle
    }

}