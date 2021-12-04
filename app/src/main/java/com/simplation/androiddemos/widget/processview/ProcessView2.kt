package com.simplation.androiddemos.widget.processview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.simplation.androiddemos.R


/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020-11-30 14:44
 * @描述:
 * @更新:
 */
class ProcessView2(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    // 点击事件的时间差
    private val TOUCH_TIME = 1000

    // 上一次点击的时间
    private var preTime = 0L

    // 当前点击的时间
    private var currTime = 0L

    //垂直方向 item 的个数
    private var verticalNum = 0

    // 水平方向 item 的个数 (与垂直方向一一对应)
    private var horizontalNum: Array<String>? = null

    // item 的宽度
    private var itemWidth = 0

    // item 的高度
    private var itemHeight = 0

    // divider 的高度
    private var verticalSpace = 0

    // item 的颜色
    private var itemColor: Int = Color.BLACK

    // item 的字体大小
    private var itemTextSize = 0f

    // item 水平间距
    private var horizontalSpace = 30

    private var textHeight = 0f

    private val path: Path = Path()
    private val paint: Paint = Paint()
    private val linkPaint: Paint = Paint()
    private val circlePaint: Paint = Paint()

    private var lastItemRectF: RectF? = null

    // 用来保存每隔 item 的 RectF，用于后面的点击事件判断
    private val rects: HashMap<Int, List<RectF>> = HashMap()

    private var texts: HashMap<Int, List<String>>? = null
    private var isRecorder: HashMap<Int, List<Boolean>>? = null

    private var listener: OnItemClickListener? = null
    private val mBigRadius = 18f // 设置画两个小圆圈时

    private val mSmallRadius = 14f

    init {
        initView(attributeSet)
    }

    private fun initView(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProcessView)
            itemColor = typedArray.getColor(R.styleable.ProcessView_itemColor, Color.BLACK)
            verticalNum = typedArray.getInt(R.styleable.ProcessView_verticalNum, 0)
            itemTextSize = typedArray.getDimension(R.styleable.ProcessView_itemTextSize, 16f)
            itemHeight = typedArray.getDimension(R.styleable.ProcessView_itemHeight, 30f).toInt()
            itemWidth = typedArray.getDimension(R.styleable.ProcessView_itemWidth, 150f).toInt()
            verticalSpace =
                typedArray.getDimension(R.styleable.ProcessView_itemVerticalSpace, 30f).toInt()
            horizontalSpace =
                typedArray.getDimension(R.styleable.ProcessView_itemHorizontalSpace, 30f).toInt()
            horizontalNum =
                typedArray.getString(R.styleable.ProcessView_horizontalNum)!!.split(",".toRegex())
                    .toTypedArray()
            typedArray.recycle()
        }
        paint.isAntiAlias = true
        paint.color = itemColor
        paint.textSize = itemTextSize
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        linkPaint.isAntiAlias = true
        linkPaint.strokeWidth = 1.5f
        linkPaint.style = Paint.Style.STROKE
        linkPaint.color = Color.parseColor("#2db1ed")
        linkPaint.pathEffect = DashPathEffect(floatArrayOf(8f, 4f), 0F)
        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = 1.5f
        circlePaint.color = Color.parseColor("#00a0e9")
        textHeight =
            paint.fontMetrics.descent - paint.fontMetrics.ascent + paint.fontMetrics.leading
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureSize(0, widthMeasureSpec), measureSize(1, heightMeasureSpec))
    }

    /**
     * 测量 子 View 宽高
     */
    private fun measureSize(type: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        var newSize = 0
        if (mode == MeasureSpec.EXACTLY) {
            newSize = size
            if (type == 0) {
                //horizontalSpace = (newSize - verticalNum * itemHeight) / (verticalNum - 1);
            } else {
                verticalSpace = (newSize - verticalNum * itemHeight) / (verticalNum - 1)
            }
        } else if (mode == MeasureSpec.AT_MOST) {
            if (type == 0) {
                //newSize = verticalNum * (itemHeight + verticalSpace);
            } else {
                newSize = verticalNum * (itemHeight + verticalSpace)
            }
        }
        return newSize
    }

    override fun onDraw(canvas: Canvas) {
        for (vIndex in 0 until verticalNum) {
            val list: MutableList<RectF> = ArrayList()
            val hNum = horizontalNum!![vIndex].toInt()
            // item 实际宽度
            val tempWidth = (width - horizontalSpace * (hNum - 1)) / hNum
            for (hIndex in 0 until hNum) {
                val rectBorder = RectF()
                val str = if (texts != null) texts!![vIndex]!![hIndex] else "----"
                rectBorder.top = (vIndex * itemHeight + vIndex * verticalSpace).toFloat()
                rectBorder.bottom = rectBorder.top + itemHeight
                if (tempWidth >= itemWidth) {
                    // 距离两边的距离
                    val margin = (width - (itemWidth * hNum + (hNum - 1) * horizontalSpace)) / 2
                    rectBorder.left =
                        (margin + hIndex * itemWidth + hIndex * horizontalSpace).toFloat()
                    rectBorder.right = itemWidth + rectBorder.left
                } else {
                    rectBorder.left = (hIndex * tempWidth + hIndex * horizontalSpace).toFloat()
                    rectBorder.right = tempWidth + rectBorder.left
                }
                val recorder =
                    if (isRecorder == null || isRecorder!!.size == 0) false else isRecorder!![vIndex]!![hIndex]
                drawBackground(canvas, rectBorder, recorder)
                drawContent(canvas, rectBorder, str, recorder)
                drawCircle(canvas, rectBorder, hNum, vIndex)
                drawLinkLine(canvas, rectBorder, hNum, vIndex)
                list.add(rectBorder)
            }
            rects[vIndex] = list
        }
    }

    /***
     * 画背景
     * @param isRecordColor
     */
    private fun drawBackground(canvas: Canvas, rect: RectF, isRecordColor: Boolean) {
        paint.color =
            if (isRecordColor) Color.parseColor("#00a0e9") else Color.parseColor("#e0f3fc")
        canvas.drawRoundRect(rect, 35f, 35f, paint)
    }

    /***
     * 画文字
     * @param isRecordColor
     */
    private fun drawContent(canvas: Canvas, rect: RectF, str: String, isRecordColor: Boolean) {
        paint.color =
            if (isRecordColor) Color.parseColor("#ffffff") else Color.parseColor("#00a0e9")
        //        paint.setColor(Color.WHITE);
        canvas.drawText(
            str,
            (rect.right + rect.left) / 2f,
            (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.fontMetrics.descent,
            paint
        )
    }

    /***
     * 画连接线
     * @param rect     当前 Item 的 RectF
     * @param currHNum 当前的水平方向 有多少个 Item
     * @param vIndex   当前是垂直方向的第几个
     */
    private fun drawLinkLine(canvas: Canvas, rect: RectF, currHNum: Int, vIndex: Int) {
        val stopX = getX(rect)
        val startX = getX(rect)
        val hIndex = horizontalNum!![vIndex].toInt()
        /**
         * 连接线(下半部分)
         * vIndex != 2 （处理部分重复的连线，可根据需求修改）
         */
        if (vIndex < verticalNum - 1) {
            // 画垂直连接线
            path.moveTo(startX.toFloat(), rect.bottom)
            path.lineTo(
                stopX.toFloat(),
                if (hIndex != 1) rect.bottom + verticalSpace / 2f - mBigRadius else rect.bottom + verticalSpace / 2f
            )

            // 画水平连接线
            if (lastItemRectF != null && lastItemRectF!!.top == rect.top && vIndex != 2) {
                path.moveTo(
                    getX(lastItemRectF!!) + mBigRadius,
                    lastItemRectF!!.bottom + verticalSpace / 2f
                )
                path.lineTo(getX(rect) - mBigRadius, rect.bottom + verticalSpace / 2f)
            }
        }
        /**
         * 画连接线(上半部分)
         * vIndex != 6（处理部分重复的连线，可根据需求修改）
         */
        if (vIndex != 0) {
            // 垂直连接线
            path.moveTo(
                startX.toFloat(),
                if (hIndex != 1) rect.top - verticalSpace / 2f + mBigRadius else rect.top - verticalSpace / 2f
            )
            path.lineTo(stopX.toFloat(), rect.top)

            // 水平连接线
            if (lastItemRectF != null && lastItemRectF!!.top == rect.top && vIndex != 6) {
                path.moveTo(
                    getX(lastItemRectF!!) + mBigRadius,
                    lastItemRectF!!.top - verticalSpace / 2f
                )
                path.lineTo(getX(rect) - mBigRadius, rect.top - verticalSpace / 2f)
            }
        }
        canvas.drawPath(path, linkPaint)
        lastItemRectF = rect
    }

    /***
     * 画相交处的圆
     * vIndex != 2（处理部分重复的连线，可根据需求修改）
     */
    private fun drawCircle(canvas: Canvas, rect: RectF, currHNum: Int, vIndex: Int) {
        if (vIndex > 0 && vIndex < verticalNum - 1 && currHNum > 1) {
            circlePaint.style = Paint.Style.FILL
            canvas.drawCircle(
                getX(rect).toFloat(),
                rect.top - verticalSpace / 2f,
                mSmallRadius,
                circlePaint
            )
            if (vIndex != 2) {
                canvas.drawCircle(
                    getX(rect).toFloat(),
                    rect.bottom + verticalSpace / 2f, mSmallRadius, circlePaint
                )
            }
            circlePaint.style = Paint.Style.STROKE
            canvas.drawCircle(
                getX(rect).toFloat(),
                rect.top - verticalSpace / 2f,
                mBigRadius,
                circlePaint
            )
            if (vIndex != 2) {
                canvas.drawCircle(
                    getX(rect).toFloat(),
                    rect.bottom + verticalSpace / 2f,
                    mBigRadius,
                    circlePaint
                )
            }
        }
    }

    private fun getX(rect: RectF): Int {
        return ((rect.left + rect.right) / 2f).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> preTime = System.currentTimeMillis()
            MotionEvent.ACTION_UP -> {
                currTime = System.currentTimeMillis()
                val result = isTouchPointInView(event)
                if (currTime - preTime < TOUCH_TIME && result.isTouchInView) {
                    listener!!.onItemClick(
                        result.vPosition, result.hPosition,
                        texts!![result.vPosition]!![result.hPosition]
                    )
                }
            }
        }
        return true
    }

    /**
     * 判断点击事件是否在某个 Item 上面
     */
    private fun isTouchPointInView(event: MotionEvent): Result {
        if (rects.size == 0) return Result(false, 0, 0)
        val eventX = event.rawX.toInt()
        val eventY = event.y.toInt()
        for (vIndex in 0 until rects.size) {
            val list = rects[vIndex]
            for (hIndex in list!!.indices) {
                val rect = list[hIndex]
                if (rect.contains(eventX.toFloat(), eventY.toFloat())) {
                    return Result(true, vIndex, hIndex)
                }
            }
        }
        return Result(false, 0, 0)
    }

    /***
     * 填充数据 设置进度
     * @param data
     * @param recorder
     */
    fun setData(data: HashMap<Int, List<String>>?, recorder: HashMap<Int, List<Boolean>>?) {
        texts = data
        isRecorder = recorder
        invalidate()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(vPosition: Int, hPosition: Int, text: String?)
    }

    internal class Result(var isTouchInView: Boolean, var vPosition: Int, var hPosition: Int)

}