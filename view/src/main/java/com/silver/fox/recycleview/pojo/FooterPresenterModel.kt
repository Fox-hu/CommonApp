package com.silver.fox.recycleview.pojo

import android.view.Gravity
import android.view.View
import com.silver.fox.ext.getString
import com.silver.fox.view.R

/**
 * @Author fox.hu
 * @Date 2020/5/15 15:11
 */
class FooterPresenterModel(var gravity: Int = Gravity.CENTER, var visibility: Int = View.VISIBLE) :
    Cloneable {
    var loadingText: String = R.string.view_common_loading.getString()
    var errorText: String = R.string.view_error_click_to_reload.getString()
    var completeText: String = R.string.view_common_loading_complete.getString()

    public override fun clone(): Any {
        return super.clone() as FooterPresenterModel
    }
}