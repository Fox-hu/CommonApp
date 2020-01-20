package com.component.kotlintest.design_patterns

import java.util.*

//访问者抽象
interface Visitor {
    fun visitor(engineer: Engineer)
    fun visitor(manager: Manager1)
}

//数据类抽象
abstract class Staff(val name: String) {
    var kpi: Int = Random().nextInt(10)

    abstract fun accept(visitor: Visitor)
}

//具体数据类
class Engineer(
    name: String,
    val codeLines: Int = Random().nextInt(10 * 10000)
) : Staff(name) {
    override fun accept(visitor: Visitor) {
        visitor.visitor(this)
    }
}

//具体数据类
class Manager1(
    name: String,
    val products: Int = Random().nextInt(10)
) : Staff(name) {

    override fun accept(visitor: Visitor) {
        visitor.visitor(this)
    }
}

//具体访问者
class CeoVisitor : Visitor {
    override fun visitor(engineer: Engineer) {
        println("工程师：${engineer.name} ,kpi = ${engineer.kpi}")
    }

    override fun visitor(manager: Manager1) {
        println("经理：${manager.name} ,kpi = ${manager.kpi},product = ${manager.products}")
    }
}

//具体访问者
class CtoVisitor : Visitor {
    override fun visitor(engineer: Engineer) {
        println("工程师：${engineer.name} , codeLines = ${engineer.codeLines}")
    }

    override fun visitor(manager: Manager1) {
        println("经理：${manager.name} , products = ${manager.products}")
    }
}

fun main() {
    val staffs = LinkedList<Staff>()
    staffs += Manager1("王经理")
    staffs += Engineer("工程师-li")
    staffs += Engineer("工程师-zhang")
    staffs += Engineer("工程师-wang")
    staffs += Engineer("工程师-zhou")

    val ctoVisitor = CtoVisitor()
    val ceoVisitor = CeoVisitor()

    println("给cto查看的报表")
    staffs.forEach { it.accept(ctoVisitor) }
    println("给ceo查看的报表")
    staffs.forEach { it.accept(ceoVisitor) }
}