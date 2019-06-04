package com.component.kotlintest.first

class Turtle {
    fun penDown() = "penDown"
    fun penUp() = "penUp"
    fun turn(degrees: Double) = degrees * 2
    fun forward(pixels: Double) = pixels * 3

    //对一个实例对象调用多个方法
    fun withTest() {
        val myTurtle = Turtle()
        with(myTurtle) {
            penDown()
            for (i in 1..4) {
                forward(100.0)
                turn(90.0)
            }
            penUp()
        }
    }
}


