package com.component.kotlintest

import com.component.kotlintest.first.*
import org.junit.Test

class Test {

    fun foo(int: Int) {
        print(int)
    }

    @Test
    fun test1() {
        listOf<Int>(1, 2, 3).forEach { foo(it) }
    }

    @Test
    fun test2() {
        val list = listOf<Int>(1, 2, 3, 4, 5, 6, 7)
        val filter = list.filter { it % 2 == 0 }
        System.out.print(filter)
    }

    @Test
    fun test3() {
        val kotlinSkill = KotlinSkill()
        kotlinSkill.keymapInvoke(1)
    }

    @Test
    fun test4() {
        val user4 = User4()
        user4.name = "fox"
    }

    @Test
    fun test5() {
        val user5 = User5(mapOf("name" to "fox", "age" to 29))
        println("name = ${user5.name},age = ${user5.age}")
    }

    @Test
    fun test6() {
        Greeter("zhangsan")("hhh")
    }

    @Test
    fun test7() {
        val i1 = Issue("IDEA-154446", "IDEA", "Bug", "Major", "Save settings failed")
        val i2 = Issue(
            "KT-12183",
            "Kotlin",
            "Feature",
            "Normal",
            "Intention: convert several calls on the same receiver to with/apply"
        )
        val predicate = ImportIssuesPredicate("Major")

        // 查找满足过滤条件的所有Issue对象，过滤条件在 invoke中
        for (issue in listOf(i1, i2).filter(predicate)) {
            println(issue.id)
        }
    }
}