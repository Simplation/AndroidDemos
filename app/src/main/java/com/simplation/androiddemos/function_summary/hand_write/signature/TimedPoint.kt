package com.simplation.androiddemos.function_summary.hand_write.signature

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @作者: Simplation
 * @日期: 2020/3/20
 * @描述:
 * @更新:
 */
class TimedPoint(val x: Float, val y: Float) {
    private val timestamp: Long = System.currentTimeMillis()

    fun velocityFrom(start: TimedPoint): Float {
        val velocity = distanceTo(start) / (timestamp - start.timestamp)
        return if (velocity != velocity) 0f else velocity
    }

    private fun distanceTo(point: TimedPoint): Float {
        return sqrt(
            (point.x - x).toDouble().pow(2.0) + (point.y - y).toDouble().pow(2.0)
        )
            .toFloat()
    }

}
