package com.component.kotlintest.demo.kotlin_demo

class KtList {
    val list = listOf(1, 3, 5, 7, 9)
    val nullableList = listOf(1, 3, 5, 7, 9, 2, null)
    val mapList = listOf(1, 3, 5, -1, 7, 9, 2)
    val negativeNullableList = listOf(1, 3, 5, -1, 7, 9, null, 2)


    fun lisTest() {
        //forEach
        list.forEach { println(it + 1) } //2 4 6 8 10

        //forEachIndexed
        list.forEachIndexed { index, value -> println("$index value is $value") }
        //0 value is 1
        //1 value is 3
        //2 value is 5
        //3 value is 7
        //4 value is 9

        //max
        println(list.max()) //9
        //maxby
        println(list.maxBy { -it }) //1

        //min
        println(list.min()) //1
        //minby
        println(list.minBy { -it }) //9

        //none
        println(list.none { it > 10 }) //true

        //count
        println(list.count { it > 7 })  //1
        println(list.count { it > 2 })  //4

        //all
        println(list.all { it > 13 })  //false
        println(list.all { it > 0 })   //true

        //any
        println(list.any { it > 13 })  //false
        println(list.any { it > 7 })   //true

        //fold
        println(list.fold(2) { total, next->
            println("$next , $total")
            next + total
        })
        //1 , 2
        //3 , 3
        //5 , 6
        //7 , 11
        //9 , 18
        //27

        //reduce
        println(list.reduce { total, next ->
            println("$next , $total")
            total + next
        })

        //sumBy
        println(list.sumBy { it + 1 }) //30

        //drop
        println(list.drop(2)) //[5, 7, 9]

        //dropWhile
        println(list.dropWhile { it < 4 }) //[5, 7, 9]

        //dropLastWhile
        val dropLastWhileList = listOf(10, 1, 3, 5, 7, 9)
        println(dropLastWhileList.dropLastWhile { it > 4 }) //[10, 1, 3]

        //filter 过滤所有符合给定函数条件的元素
        println(list.filter { it < 4 }) //[1, 3]

        //filterNot 过滤所有不符合给定函数条件的元素
        println(list.filterNot { it < 4 }) //[5, 7, 9]

        //filterNotNull 过滤所有元素中不是null的元素
        println(nullableList.filterNotNull()) //[1, 3, 5, 7, 9]

        //slice 过滤一个list中指定index的元素
        println(nullableList.slice(listOf(0, 3))) //[1, 7]

        //take 返回从第一个开始的n个元素
        println(nullableList.take(2)) //[1, 3]

        //takeLast 返回从最后一个开始的n个元素
        println(nullableList.takeLast(2)) //[2, null]

        //takeWhile 返回从第一个开始符合给定函数条件的元素。
        val takeWhileList = listOf(1, 3, 5, -1, 7, 9, 2)
        println(takeWhileList.takeWhile { it > 2 }) //[]
        println(takeWhileList.takeWhile { it > 0 }) //[1, 3, 5]

        //flatMap 遍历所有的元素，为每一个创建一个集合，最后把所有的集合放在一个集合中
        println(mapList.flatMap { listOf(it, it + 1) }) //[1, 2, 3, 4, 5, 6, -1, 0, 7, 8, 9, 10, 2, 3]

        //groupBy 返回一个根据给定函数分组后的map
        println(mapList.groupBy { listOf(it) }) //{[1]=[1], [3]=[3], [5]=[5], [-1]=[-1], [7]=[7], [9]=[9], [2]=[2]}
        println(mapList.groupBy { listOf(it, it + 1) }) //{[1, 2]=[1], [3, 4]=[3], [5, 6]=[5], [-1, 0]=[-1], [7, 8]=[7], [9, 10]=[9], [2, 3]=[2]}

        //map 返回一个每一个元素根据给定的函数转换所组成的List
        println(mapList.map { listOf(it) }) //[[1], [3], [5], [-1], [7], [9], [2]]
        println(mapList.map { listOf(it, it + 1) }) //[[1, 2], [3, 4], [5, 6], [-1, 0], [7, 8], [9, 10], [2, 3]]

        //mapIndexed 返回一个每一个元素根据给定的包含元素index的函数转换所组成的List
        println(mapList.mapIndexed { index, value -> index }) //[0, 1, 2, 3, 4, 5, 6]
        println(mapList.mapIndexed { index, value -> index * value }) //[0, 3, 10, -3, 28, 45, 12]

        //mapNotNull 返回一个每一个非null元素根据给定的函数转换所组成的List
        println(mapList.mapNotNull { it }) //[1, 3, 5, -1, 7, 9, 2]
    }

}