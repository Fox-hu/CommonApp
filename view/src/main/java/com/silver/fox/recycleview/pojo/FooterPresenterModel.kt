package com.silver.fox.recycleview.pojo

import android.view.Gravity
import android.view.View
import com.silver.fox.ext.getString

/**
 * @Author fox.hu
 * @Date 2020/5/15 15:11
 */
class FooterPresenterModel(var gravity: Int = Gravity.CENTER, var visibility: Int = View.VISIBLE) :
    Cloneable {
    var text: String = ""

    fun setText(id: Int) {
        text = id.getString()
    }

    public override fun clone(): Any {
        return super.clone() as FooterPresenterModel
    }
}