package com.fox.authandshare.share

import android.os.Bundle

/**
 * @author fox.hu
 * @date 2018/9/4
 */

interface ShareParamsHelper<T> {

    fun createParams(type: ShareType, t: T): Bundle
}
