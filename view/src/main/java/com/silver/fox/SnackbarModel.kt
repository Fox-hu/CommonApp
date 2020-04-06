package com.silver.fox

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.silver.fox.ext.getColor
import com.silver.fox.ext.getString
import com.silver.fox.view.R

class SnackbarModel
constructor(
    var content: String? = "",
    @ColorInt var contentBgColor: Int = R.color.view_app_colorPrimaryDark.getColor(),
    @ColorInt var contentColor: Int = R.color.view_white.getColor(),
    @ColorInt var actionColor: Int = R.color.view_app_colorAccent.getColor(),
    var duration: Int = Snackbar.LENGTH_SHORT,
    var actionText: String? = null,
    var onAction: View.OnClickListener? = null,
    var onCallback: Snackbar.Callback? = null
) {
    constructor(@StringRes resId: Int) : this(resId.getString())
}


fun String?.toSnackbarMsg(): SnackbarModel =
    SnackbarModel(this.orEmpty())
