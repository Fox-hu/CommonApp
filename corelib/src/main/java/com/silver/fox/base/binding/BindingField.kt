package com.silver.fox.base.binding


import androidx.databinding.ObservableField

open class BindingField<T>

//这个注解会暴露多个重载函数给java
@JvmOverloads
constructor(value: T? = null, private var onChange: onFieldChangeListener<T>? = null) :
    ObservableField<T>(value) {

    override fun set(value: T) {
        var changed = get() != value
        super.set(value)

        if (changed) {
            onChange?.invoke(this, value)
        }
    }
}

typealias onFieldChangeListener<T> = (BindingField<T>, T) -> Unit