package com.component.kotlintest.design_patterns

/**
 * 一个命令模式的案例 俄罗斯方块
 * @Author fox.hu
 * @Date 2020/1/13 14:27
 */

interface Command {
    fun execute()
}

class TetrisMachine {
    fun toLeft() = "toLeft".logi()

    fun toRight() = "toRight".logi()

    fun fastToBottom() = "fastToBottom".logi()

    fun transform() = "transform".logi()

}

class LeftCommand(private val machine: TetrisMachine) : Command {
    override fun execute() {
        machine.toLeft()
    }
}

class RightCommand(private val machine: TetrisMachine) : Command {
    override fun execute() {
        machine.toRight()
    }
}

class FastToBottomCommand(private val machine: TetrisMachine) : Command {
    override fun execute() {
        machine.fastToBottom()
    }
}

class TransformCommand(private val machine: TetrisMachine) : Command {
    override fun execute() {
        machine.transform()
    }
}

class Buttons(
    private val leftCommand: LeftCommand,
    private val rightCommand: RightCommand,
    private val fastToBottomCommand: FastToBottomCommand,
    private val transformCommand: TransformCommand
) {
    fun toLeft() = leftCommand.execute()

    fun toRight() = rightCommand.execute()

    fun fastToBottom() = fastToBottomCommand.execute()

    fun transform() = transformCommand.execute()
}

