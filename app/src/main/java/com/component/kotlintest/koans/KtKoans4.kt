package com.component.kotlintest.koans


class PropertyExample() {
    var counter = 0
    var propertyWithCounter: Int? = null
        set(v: Int?) {
            field = v
            counter++
        }
}

class LazyProperty(val initializer: () -> Int) {
    var value: Int? = null
    val lazy: Int
        get() {
            if (value == null) {
                value = initializer()
            }
            return value!!
        }
}

class LazyProperty1(val initializer: () -> Int) {

    val lazyValue: Int by lazy(initializer)
}



