//package com.simplation.androiddemos.function_summary.hand_write.signature
//
///**
// * @作者: W ◕‿-｡ Z
// * @日期: 2020/3/20
// * @描述:
// * @更新:
// */
//class Bezier(
//    timedPoint: TimedPoint,
//    c2: TimedPoint?,
//    c3: TimedPoint?,
//    timedPoint3: TimedPoint
//) {
//    var startPoint: TimedPoint? = null
//    var control1: TimedPoint? = null
//    var control2: TimedPoint? = null
//    var endPoint: TimedPoint? = null
//
//    fun Bezier(
//        startPoint: TimedPoint?,
//        control1: TimedPoint?,
//        control2: TimedPoint?,
//        endPoint: TimedPoint?
//    ) {
//        this.startPoint = startPoint
//        this.control1 = control1
//        this.control2 = control2
//        this.endPoint = endPoint
//    }
//
//    fun length(): Float {
//        val steps = 10
//        var length = 0
//        var t: Float
//        var cx: Double
//        var cy: Double
//        var px = 0.0
//        var py = 0.0
//        var xdiff: Double
//        var ydiff: Double
//        var i: Int = 0
//        while (i <= steps) {
//            t = i / steps.toFloat()
//            cx = point(t, startPoint!!.x, control1!!.x, control2!!.x, endPoint!!.x)
//            cy = point(t, startPoint!!.y, control1!!.y, control2!!.y, endPoint!!.y)
//            if (i > 0) {
//                xdiff = cx - px
//                ydiff = cy - py
//                length += Math.sqrt(xdiff * xdiff + ydiff * ydiff).toInt()
//            }
//            px = cx
//            py = cy
//            i++
//        }
//        return length.toFloat()
//    }
//
//    fun point(
//        t: Float,
//        start: Float,
//        c1: Float,
//        c2: Float,
//        end: Float
//    ): Double {
//        return start * (1.0 - t) * (1.0 - t) * (1.0 - t) + 3.0 * c1 * (1.0 - t) * (1.0 - t) * t + (3.0 * c2 * (1.0 - t)
//                * t * t) + end * t * t * t
//    }
//}
