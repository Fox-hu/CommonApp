package com.component.kotlintest.first

class KtList {
    val list = listOf<Int>(1, 2, 3, 4, 5, 6)

    fun lisTest() {

        val map = list.map { it * 2 }

        val filter = list.filter { it % 2 == 0 }

        val filterNot = list.filterNot { it % 2 == 0 }

        val count = list.count { it % 2 == 0 }


    }
}