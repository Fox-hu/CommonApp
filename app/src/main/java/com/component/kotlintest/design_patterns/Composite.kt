package com.component.kotlintest.design_patterns

/**
 * 组合模式 用于树形结构
 * @Author fox.hu
 * @Date 2020/1/19 17:00
 */
//抽象构件,枝干和叶子节点都继承自它
abstract class Corp(var name: String = "", var position: String = "", var salary: Int = 0) {

    var parent: Corp? = null

    fun getInfo(): String {
        return "name = $name , position = $position , salary = $salary ,parent = ${parent?.name}"
    }
}

class Branch(name: String, position: String, salary: Int) : Corp(name, position, salary) {
    val subordinateList = ArrayList<Corp>()

    operator fun plusAssign(corp: Corp) {
        corp.parent = this
        subordinateList += corp
    }

    operator fun minusAssign(corp: Corp) {
        subordinateList -= corp
    }

    operator fun get(index: Int) {
        subordinateList[index]
    }
}

class Leaf(name: String, position: String, salary: Int) : Corp(name, position, salary)

fun main() {
    val ceo = Branch("张三", "总经理", 100000)
    val saleManager = Branch("李四", "销售部门经理", 50000)
    val financeManager = Branch("王二", "财务部门经理", 40000)

    val developManager = Branch("赵四", "研发部门经理", 40000)
    val groupLeader1 = Branch("钱一", "开发一组组长", 10000)
    val groupLeader2 = Branch("吴六", "开发二组组长", 10000)
    val employee1 = Branch("甲", "开发人员", 5000)
    val employee2 = Branch("乙", "开发人员", 5000)
    val employee3 = Branch("丙", "开发人员", 5000)
    ceo += saleManager
    ceo += financeManager
    ceo += developManager

    developManager += groupLeader1
    developManager += groupLeader2

    groupLeader1 += employee1
    groupLeader1 += employee2

    groupLeader2 += employee3

    println(getTreeInfo(ceo))
}

fun getTreeInfo(branch: Branch): String {
    val builder = StringBuilder()
    branch.subordinateList.forEach {
        if (it is Leaf) {
            builder.append(it.getInfo())
            builder.append("\n")
        } else if (it is Branch) {
            builder.append(it.getInfo())
            builder.append("\n")
            builder.append(getTreeInfo(it))
        }
    }
    return builder.toString()
}