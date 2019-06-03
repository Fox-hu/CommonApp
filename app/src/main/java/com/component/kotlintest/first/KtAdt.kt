package com.component.kotlintest.first

//adt为和类型
//配合sealed关键字和when来实现定义图形面积的方法
sealed class Shape {
    class Circle(val radius: Double) : Shape()
    class Rectangle(val width: Double, val height: Double) : Shape()
    class Triangle(val base: Double, val height: Double) : Shape()
}

fun getArea(shape: Shape): Double = when (shape) {
    is Shape.Circle -> Math.PI * shape.radius * shape.radius
    is Shape.Rectangle -> shape.width * shape.height
    is Shape.Triangle -> shape.base * shape.height / 2.0
}

val shape  = Shape.Rectangle(10.0,5.5)

fun test(){
    println(getArea(shape))
}