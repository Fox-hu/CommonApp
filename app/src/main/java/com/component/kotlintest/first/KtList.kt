package com.component.kotlintest.first

class KtList {
    val list = listOf<Int>(1, 2, 3, 4, 5, 6, 7)

    fun lisTest() {

        val map = list.map { it * 2 }

        val filter = list.filter { it % 2 == 0 }

        val filterNot = list.filterNot { it % 2 == 0 }

        val count = list.count { it % 2 == 0 }

        val all = list.all { it % 2 == 0 }

        val any = list.any { it % 2 == 0 }

        val find = list.find { it % 2 == 0 }

        val max = list.maxBy { it % 2 }

        val min = list.minBy { it % 2 }

        val sum = list.sumBy { it % 2 }

        val groupby = list.groupBy { it % 2 }

        val (list1, list2) = list.partition { it > 4 }
    }

}