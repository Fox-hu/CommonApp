package com.fox.authandshare.share


import com.fox.authandshare.platform.PlatForm

/**
 * Created by fox.hu on 2018/8/31.
 */

interface ShareListener {
    fun onStart(platForm: PlatForm)

    fun onComplete(response: Any)

    fun onError(errorMsg: String)

    fun onCancel()
}
