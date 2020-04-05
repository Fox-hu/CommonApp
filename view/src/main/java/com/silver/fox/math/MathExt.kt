package com.silver.fox.math

import com.silver.fox.ext.logi
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @Author fox
 * @Date 2020/4/4 17:45
 */
fun checkInRound(
    centerX: Float,
    centerY: Float,
    currentX: Float,
    currentY: Float,
    radius: Float
): Boolean {
    return sqrt((currentX - centerX) * (currentX - centerX) + (currentY - centerY) * (currentY - centerY)) < radius
}

fun distance(startX: Double, startY: Double, endX: Double, endY: Double): Double {
    return sqrt(abs(startX - endX) * abs(startX - endX) + abs(startY - endY) * abs(startY - endY))
}