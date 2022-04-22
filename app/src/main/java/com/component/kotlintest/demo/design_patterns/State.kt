package com.component.kotlintest.demo.design_patterns

/**
 * 状态模式的案例 电梯
 * @Author fox.hu
 * @Date 2020/1/16 15:04
 */

interface State {
    var stateContext: StateContext?

    fun open() = Unit

    fun close() = Unit

    fun run() = Unit

    fun stop() = Unit
}

class OpenState : State {

    override var stateContext: StateContext? = null

    override fun close() {
        stateContext?.apply {
            state = StateContext.closeState
            close()
        }
    }

    override fun open() {
        println("电梯门开启")
    }
}

class CloseState : State {
    override var stateContext: StateContext? = null

    override fun open() {
        stateContext?.apply {
            state = StateContext.openState
            open()
        }
    }

    override fun close() {
        println("电梯门关闭")
    }

    override fun run() {
        stateContext?.apply {
            state = StateContext.runState
            run()
        }
    }

    override fun stop() {
        stateContext?.apply {
            state = StateContext.stopState
            stop()
        }
    }
}

class runState : State {
    override var stateContext: StateContext? = null

    override fun run() {
        println("电梯上下运行")
    }

    override fun stop() {
        stateContext?.apply {
            state = StateContext.stopState
            stop()
        }
    }
}

class StopState : State {
    override var stateContext: StateContext? = null

    override fun open() {
        stateContext?.apply {
            state = StateContext.openState
            open()
        }
    }

    override fun run() {
        stateContext?.apply {
            state = StateContext.runState
            run()
        }
    }

    override fun stop() {
        println("电梯停止运行")
    }
}

class StateContext : State {
    companion object {
        val openState: OpenState = OpenState()
        val closeState: CloseState = CloseState()
        val runState: runState = runState()
        val stopState: StopState = StopState()
    }

    init {
        openState.stateContext = this
        closeState.stateContext = this
        runState.stateContext = this
        stopState.stateContext = this
    }

    var state: State = closeState

    override var stateContext: StateContext?
        get() = this
        set(value) {}

    override fun open() {
        state.open()
    }

    override fun close() {
        state.close()
    }

    override fun run() {
        state.run()
    }

    override fun stop() {
        state.stop()
    }
}

fun main() {
    val context = StateContext().run {
        open()
        close()
        run()
        stop()
    }
}
