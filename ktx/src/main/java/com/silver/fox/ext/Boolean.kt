package com.silver.fox.ext

//如果为null 则为true
fun Boolean?.orTrue() = this ?: true