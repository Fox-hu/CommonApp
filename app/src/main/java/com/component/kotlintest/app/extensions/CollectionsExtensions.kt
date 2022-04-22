package com.component.kotlintest.app.extensions

import java.util.*


inline fun <T, R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R {
    for (element in this) {
        val result = predicate(element)
        if (result != null) return result
    }
    throw NoSuchElementException("No element matching predicate was found.")
}

inline fun <K, V, R : Any> Map<K, V>.firstResult(predicate: (K, V) -> R?): R {
    for ((key, value) in this) {
        val result = predicate(key, value)
        result?.let { result }
    }
    throw NoSuchElementException("No entry matching predicate was found.")
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> = map { Pair(it.key, it.value!!) }.toTypedArray()