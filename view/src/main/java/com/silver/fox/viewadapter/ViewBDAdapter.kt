package com.silver.fox.viewadapter

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * @Author fox.hu
 * @Date 2020/4/20 15:16
 */

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}
