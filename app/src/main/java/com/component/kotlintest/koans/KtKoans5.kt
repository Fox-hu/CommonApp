package com.component.kotlintest.koans

//task1
fun task(): List<Boolean> {
    val isEven: Int.() -> Boolean = { this % 2 == 0 }
    val isOdd: Int.() -> Boolean = { this % 2 != 0 }
    return listOf(42.isOdd(), 239.isOdd(), 294823098.isEven())
}

//kotlin的map是不可变的 没有put方法 只有MutableMap及其实现类有put方法
//这个特性是匿名扩展方法
fun buildMap(map: HashMap<Int, String>.() -> Unit): HashMap<Int, String> {
    val hashMap = HashMap<Int, String>()
    hashMap.map()
    return hashMap
}

fun usage(): Map<Int, String> {
    return buildMap {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}

//aplly的用法
fun <T> T.myApply(f: T.() -> Unit): T {
    return apply(f)
}

fun createString(): String {
    return StringBuilder().myApply {
        append("Numbers: ")
        for (i in 1..10) {
            append(i)
        }
    }.toString()
}

fun createMap(): Map<Int, String> {
    return hashMapOf<Int, String>().myApply {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}

fun <T, C : MutableCollection<T>> Collection<T>.partitionTo(
    first: C,
    second: C,
    predicate: (T) -> Boolean
): Pair<C, C> {
    for (element in this) {
        if (predicate(element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}

fun partitionWordsAndLines() {
    val (words, lines) = listOf("a", "a b", "c", "d e").partitionTo(
        ArrayList<String>(),
        ArrayList()
    ) { s -> !s.contains(" ") }
    words == listOf("a", "c")
    lines == listOf("a b", "d e")
}

fun partitionLettersAndOtherSymbols() {
    val (letters, other) = setOf('a', '%', 'r', '}').partitionTo(
        HashSet<Char>(),
        HashSet()
    ) { c -> c in 'a'..'z' || c in 'A'..'Z' }
    letters == setOf('a', 'r')
    other == setOf('%', '}')
}


