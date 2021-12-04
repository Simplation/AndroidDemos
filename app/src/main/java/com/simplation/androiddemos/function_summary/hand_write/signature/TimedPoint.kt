//package com.simplation.androiddemos.function_summary.hand_write.signature
//
//import kotlin.math.pow
//import kotlin.math.sqrt
//
///**
// * @作者: W ◕‿-｡ Z
// * @日期: 2020/3/20
// * @描述:
// * @更新:
// */
//class TimedPoint(x: Float, y: Float) {
//    var x = 0f
//    var y = 0f
//    var timestamp: Long = 0
//
// /*   fun TimedPoint(x: Float, y: Float) {
//        this.x = x
//        this.y = y
//        timestamp = System.currentTimeMillis()
//    }*/
//
//    fun velocityFrom(start: TimedPoint): Float {
//        val velocity = distanceTo(start) / (timestamp - start.timestamp)
//        return if (velocity != velocity) 0f else velocity
//    }
//
//    fun distanceTo(point: TimedPoint): Float {
//        return sqrt(
//            (point.x - x.toDouble()).pow(2.0) + (point.y - y.toDouble()).pow(2.0)
//        ).toFloat()
//    }
//}