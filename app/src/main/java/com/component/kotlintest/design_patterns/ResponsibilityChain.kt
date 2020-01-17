package com.component.kotlintest.design_patterns

/**
 * 一个责任链模式的案例 报销金额
 * 不同的职位所能处理的报销金额不同
 * @Author fox.hu
 * @Date 2020/1/17 13:30
 */

abstract class Leader {
    internal var nextLeader: Leader? = null

    fun handleRequest(money: Int): Boolean {
        return if (money <= limit) {
            handle(money)
        } else {
            nextLeader?.handleRequest(money) ?: false
        }
    }

    abstract fun handle(money: Int): Boolean

    abstract val limit: Int
}

class GroupLeader : Leader() {
    override fun handle(money: Int): Boolean {
        println("主管已批复")
        return true
    }

    override val limit: Int = 5000
}

class Manager : Leader() {
    override fun handle(money: Int): Boolean {
        println("经理已批复")
        return true
    }

    override val limit: Int = 20000
}

class Boss : Leader() {
    override fun handle(money: Int): Boolean {
        println("老板已批复")
        return true
    }

    override val limit: Int = Int.MAX_VALUE
}

fun main() {
    val manager = Manager()
    val groupLeader = GroupLeader()
    val boss = Boss()
    groupLeader.nextLeader = manager
    manager.nextLeader = boss
    groupLeader.handleRequest(50000)
}

