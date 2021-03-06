package com.simplation.androiddemos.function_summary.hand_write.signature

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import com.simplation.androiddemos.R
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * @作者: Simplation
 * @日期: 2020/3/20
 * @描述:
 * @更新:
 */
class SignatureView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    // View state
    private var mPoints: MutableList<TimedPoint>? = null
    private var mIsEmpty = false
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var mLastVelocity = 0f
    private var mLastWidth = 0f
    private val mDirtyRect: RectF

    // Configurable parameters
    private var mMinWidth = 0
    private var mMaxWidth = 0
    private var mVelocityFilterWeight = 0f
    private var mOnSignedListener: OnSignedListener? = null
    private val mPaint = Paint()
    private val mPath = Path()
    private var mSignatureBitmap: Bitmap? = null
    private var mSignatureBitmapCanvas: Canvas? = null

    /**
     * Set the pen color from a given resource. If the resource is not found,
     * [Color.BLACK] is assumed.
     *
     * @param colorRes the color resource.
     */
    @SuppressLint("ResourceType")
    fun setPenColorRes(colorRes: Int) {
        try {
            setPenColor(resources.getColor(colorRes))
        } catch (ex: NotFoundException) {
            setPenColor(resources.getColor(Color.BLACK))
        }
    }

    /**
     * Set the pen color from a given color.
     *
     * @param color the color.
     */
    private fun setPenColor(color: Int) {
        mPaint.color = color
    }

    /**
     * Set the minimum width of the stroke in pixel.
     *
     * @param minWidth the width in dp.
     */
    fun setMinWidth(minWidth: Float) {
        mMinWidth = convertDpToPx(minWidth)
    }

    /**
     * Set the maximum width of the stroke in pixel.
     *
     * @param maxWidth the width in dp.
     */
    fun setMaxWidth(maxWidth: Float) {
        mMaxWidth = convertDpToPx(maxWidth)
    }

    /**
     * Set the velocity filter weight.
     *
     * @param velocityFilterWeight the weight.
     */
    fun setVelocityFilterWeight(velocityFilterWeight: Float) {
        mVelocityFilterWeight = velocityFilterWeight
    }

    fun clear() {
        mPoints = ArrayList()
        mLastVelocity = 0f
        mLastWidth = ((mMinWidth + mMaxWidth) / 2).toFloat()
        mPath.reset()
        if (mSignatureBitmap != null) {
            mSignatureBitmap = null
            ensureSignatureBitmap()
        }
        setIsEmpty(true)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                mPoints!!.clear()
                mPath.moveTo(eventX, eventY)
                mLastTouchX = eventX
                mLastTouchY = eventY
                addPoint(TimedPoint(eventX, eventY))
                resetDirtyRect(eventX, eventY)
                addPoint(TimedPoint(eventX, eventY))
            }
            MotionEvent.ACTION_MOVE -> {
                resetDirtyRect(eventX, eventY)
                addPoint(TimedPoint(eventX, eventY))
            }
            MotionEvent.ACTION_UP -> {
                resetDirtyRect(eventX, eventY)
                addPoint(TimedPoint(eventX, eventY))
                parent.requestDisallowInterceptTouchEvent(true)
                setIsEmpty(false)
            }
            else -> return false
        }

        // invalidate();
        invalidate(
            (mDirtyRect.left - mMaxWidth).toInt(), (mDirtyRect.top - mMaxWidth).toInt(),
            (mDirtyRect.right + mMaxWidth).toInt(), (mDirtyRect.bottom + mMaxWidth).toInt()
        )
        return true
    }

    override fun onDraw(canvas: Canvas) {
        if (mSignatureBitmap != null) {
            canvas.drawBitmap(mSignatureBitmap!!, 0f, 0f, mPaint)
        }
    }

    fun setOnSignedListener(listener: OnSignedListener?) {
        mOnSignedListener = listener
    }

    fun isEmpty(): Boolean {
        return mIsEmpty
    }

    fun getSignatureBitmap(): Bitmap {
        val originalBitmap = getTransparentSignatureBitmap()
        val whiteBgBitmap = Bitmap.createBitmap(
            originalBitmap!!.width, originalBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        return whiteBgBitmap
    }

    fun setSignatureBitmap(signature: Bitmap) {
        clear()
        ensureSignatureBitmap()
        val tempSrc = RectF()
        val tempDst = RectF()
        val dWidth = signature.width
        val dHeight = signature.height
        val vWidth = width
        val vHeight = height

        // Generate the required transform.
        tempSrc[0f, 0f, dWidth.toFloat()] = dHeight.toFloat()
        tempDst[0f, 0f, vWidth.toFloat()] = vHeight.toFloat()
        val drawMatrix = Matrix()
        drawMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER)
        val canvas = Canvas(mSignatureBitmap!!)
        canvas.drawBitmap(signature, drawMatrix, null)
        setIsEmpty(false)
        invalidate()
    }

    private fun getTransparentSignatureBitmap(): Bitmap? {
        ensureSignatureBitmap()
        return mSignatureBitmap
    }

    fun getTransparentSignatureBitmap(trimBlankSpace: Boolean): Bitmap? {
        if (!trimBlankSpace) {
            return getTransparentSignatureBitmap()
        }
        ensureSignatureBitmap()
        val imgHeight = mSignatureBitmap!!.height
        val imgWidth = mSignatureBitmap!!.width
        val backgroundColor = Color.TRANSPARENT
        var xMin = Int.MAX_VALUE
        var xMax = Int.MIN_VALUE
        var yMin = Int.MAX_VALUE
        var yMax = Int.MIN_VALUE
        var foundPixel = false

        // Find xMin
        for (x in 0 until imgWidth) {
            var stop = false
            for (y in 0 until imgHeight) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    xMin = x
                    stop = true
                    foundPixel = true
                    break
                }
            }
            if (stop) break
        }

        // Image is empty...
        if (!foundPixel) return null

        // Find yMin
        for (y in 0 until imgHeight) {
            var stop = false
            for (x in xMin until imgWidth) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    yMin = y
                    stop = true
                    break
                }
            }
            if (stop) break
        }

        // Find xMax
        for (x in imgWidth - 1 downTo xMin) {
            var stop = false
            for (y in yMin until imgHeight) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    xMax = x
                    stop = true
                    break
                }
            }
            if (stop) break
        }

        // Find yMax
        for (y in imgHeight - 1 downTo yMin) {
            var stop = false
            for (x in xMin..xMax) {
                if (mSignatureBitmap!!.getPixel(x, y) != backgroundColor) {
                    yMax = y
                    stop = true
                    break
                }
            }
            if (stop) break
        }
        return Bitmap.createBitmap(mSignatureBitmap!!, xMin, yMin, xMax - xMin, yMax - yMin)
    }

    private fun addPoint(newPoint: TimedPoint) {
        mPoints!!.add(newPoint)
        if (mPoints!!.size > 2) {
            // To reduce the initial lag make it work with 3 mPoints
            // by copying the first point to the beginning.
            if (mPoints!!.size == 3) mPoints!!.add(0, mPoints!![0])
            var tmp = calculateCurveControlPoints(mPoints!![0], mPoints!![1], mPoints!![2])
            val c2: TimedPoint = tmp.c2
            tmp = calculateCurveControlPoints(mPoints!![1], mPoints!![2], mPoints!![3])
            val c3: TimedPoint = tmp.c1
            val curve = Bezier(mPoints!![1], c2, c3, mPoints!![2])
            val startPoint = curve.startPoint
            val endPoint = curve.endPoint
            var velocity = endPoint.velocityFrom(startPoint)
            velocity = if (java.lang.Float.isNaN(velocity)) 0.0f else velocity
            velocity =
                mVelocityFilterWeight * velocity + (1 - mVelocityFilterWeight) * mLastVelocity

            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            val newWidth = strokeWidth(velocity)

            // The Bezier's width starts out as last curve's final width, and
            // gradually changes to the stroke width just calculated. The new
            // width calculation is based on the velocity between the Bezier's
            // start and end mPoints.
            addBezier(curve, mLastWidth, newWidth)
            mLastVelocity = velocity
            mLastWidth = newWidth

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            mPoints!!.removeAt(0)
        }
    }

    private fun addBezier(curve: Bezier, startWidth: Float, endWidth: Float) {
        ensureSignatureBitmap()
        val originalWidth = mPaint.strokeWidth
        val widthDelta = endWidth - startWidth
        val drawSteps = floor(curve.length().toDouble()).toFloat()
        var i = 0
        while (i < drawSteps) {

            // Calculate the Bezier (x, y) coordinate for this step.
            val t = i.toFloat() / drawSteps
            val tt = t * t
            val ttt = tt * t
            val u = 1 - t
            val uu = u * u
            val uuu = uu * u
            var x = uuu * curve.startPoint.x
            x += 3 * uu * t * curve.control1.x
            x += 3 * u * tt * curve.control2.x
            x += ttt * curve.endPoint.x
            var y = uuu * curve.startPoint.y
            y += 3 * uu * t * curve.control1.y
            y += 3 * u * tt * curve.control2.y
            y += ttt * curve.endPoint.y

            // Set the incremental stroke width and draw.
            mPaint.strokeWidth = startWidth + ttt * widthDelta
            mSignatureBitmapCanvas!!.drawPoint(x, y, mPaint)
            expandDirtyRect(x, y)
            i++
        }
        mPaint.strokeWidth = originalWidth
    }

    private fun calculateCurveControlPoints(
        s1: TimedPoint,
        s2: TimedPoint,
        s3: TimedPoint
    ): ControlTimedPoints {
        val dx1 = s1.x - s2.x
        val dy1 = s1.y - s2.y
        val dx2 = s2.x - s3.x
        val dy2 = s2.y - s3.y
        val m1 = TimedPoint((s1.x + s2.x) / 2.0f, (s1.y + s2.y) / 2.0f)
        val m2 = TimedPoint((s2.x + s3.x) / 2.0f, (s2.y + s3.y) / 2.0f)
        val l1 = sqrt((dx1 * dx1 + dy1 * dy1).toDouble()).toFloat()
        val l2 = sqrt((dx2 * dx2 + dy2 * dy2).toDouble()).toFloat()
        val dxm = m1.x - m2.x
        val dym = m1.y - m2.y
        val k = l2 / (l1 + l2)
        val cm = TimedPoint(m2.x + dxm * k, m2.y + dym * k)
        val tx = s2.x - cm.x
        val ty = s2.y - cm.y
        return ControlTimedPoints(
            TimedPoint(m1.x + tx, m1.y + ty),
            TimedPoint(m2.x + tx, m2.y + ty)
        )
    }

    private fun strokeWidth(velocity: Float): Float {
        return (mMaxWidth / (velocity + 1)).coerceAtLeast(mMinWidth.toFloat())
    }

    /**
     * Called when replaying history to ensure the dirty region includes all
     * mPoints.
     *
     * @param historicalX the previous x coordinate.
     * @param historicalY the previous y coordinate.
     */
    private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < mDirtyRect.left) {
            mDirtyRect.left = historicalX
        } else if (historicalX > mDirtyRect.right) {
            mDirtyRect.right = historicalX
        }
        if (historicalY < mDirtyRect.top) {
            mDirtyRect.top = historicalY
        } else if (historicalY > mDirtyRect.bottom) {
            mDirtyRect.bottom = historicalY
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     *
     * @param eventX the event x coordinate.
     * @param eventY the event y coordinate.
     */
    private fun resetDirtyRect(eventX: Float, eventY: Float) {

        // The mLastTouchX and mLastTouchY were set when the ACTION_DOWN motion
        // event occurred.
        mDirtyRect.left = mLastTouchX.coerceAtMost(eventX)
        mDirtyRect.right = mLastTouchX.coerceAtLeast(eventX)
        mDirtyRect.top = mLastTouchY.coerceAtMost(eventY)
        mDirtyRect.bottom = mLastTouchY.coerceAtLeast(eventY)
    }

    private fun setIsEmpty(newValue: Boolean) {
        mIsEmpty = newValue
        if (mOnSignedListener != null) {
            if (mIsEmpty) {
                mOnSignedListener!!.onClear()
            } else {
                mOnSignedListener!!.onSigned()
            }
        }
    }

    private fun ensureSignatureBitmap() {
        if (mSignatureBitmap == null) {
            mSignatureBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mSignatureBitmapCanvas = Canvas(mSignatureBitmap as Bitmap)
        }
    }

    private fun convertDpToPx(dp: Float): Int {
        return (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    interface OnSignedListener {
        fun onSigned()
        fun onClear()
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SignatureView, 0, 0)

        // Configurable parameters
        try {
            mMinWidth =
                a.getDimensionPixelSize(R.styleable.SignatureView_minWidth, convertDpToPx(3f))
            mMaxWidth =
                a.getDimensionPixelSize(R.styleable.SignatureView_maxWidth, convertDpToPx(20f))
            mVelocityFilterWeight = a.getFloat(R.styleable.SignatureView_velocityFilterWeight, 0.9f)
            mPaint.color = a.getColor(R.styleable.SignatureView_penColor, Color.BLACK)
        } finally {
            a.recycle()
        }

        // Fixed parameters
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        //mPaint.setStyle(Paint.Style.FILL);  //画笔风格
        //		mPaint.setAntiAlias(true);          //抗锯齿
        //		mPaint.setStrokeWidth(10);           //画笔粗细
        //		mPaint.setTextSize(100);             //绘制文字大小，单位px
        //mPaint.setStrokeWidth(Paint.S);
        // Dirty rectangle to update only the changed portion of the view
        mDirtyRect = RectF()
        clear()
    }
}