package com.simplation.androiddemos.widget.processview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.simplation.androiddemos.R
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020-11-30 11:26
 * @描述:
 * @更新:
 */
class ProcessView(mContext: Context, mAttributeSet: AttributeSet) : View(mContext, mAttributeSet) {

    // 点击事件的时间差
    private val TOUCH_TIME = 1000

    // 上一次点击的时间
    private var preTime = 0L

    // 当前点击的时间
    private var currTime = 0L

    // 垂直方向 item 的个数
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

    private val paint: Paint = Paint()

    private var lastItemRect: Rect? = null

    // 用来保存每隔 item 的 Rect，用于后面的点击事件判断
    private val rects: HashMap<Int, List<Rect>> = HashMap()

    private val texts = getTextShow()

    private var listener: OnItemClickListener? = null


    init {
        initView(mAttributeSet)
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
        paint.style = Paint.Style.STROKE
        paint.textAlign = Paint.Align.CENTER
        textHeight =
            paint.fontMetrics.descent - paint.fontMetrics.ascent + paint.fontMetrics.leading
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), measureSize(1))
    }

    /**
     * 测量 子 View 宽高
     */
    private fun measureSize(i: Int): Int {
        var newSize = 0
        if (i == 0) {
            //newSize = ScreenUtils_Java.getScreenWidth(getContext());
        } else {
            newSize = verticalNum * (itemHeight + verticalSpace)
        }
        return newSize
    }

    override fun onDraw(canvas: Canvas) {
        for (vIndex in 0 until verticalNum) {
            val list: MutableList<Rect> = ArrayList()
            val hNum = horizontalNum!![vIndex].toInt()
            // item 实际宽度
            val tempWidth = (width - horizontalSpace * (hNum - 1)) / hNum
            for (hIndex in 0 until hNum) {
                val rectBorder = Rect()
                val str = texts[vIndex]!![hIndex]
                rectBorder.top = vIndex * itemHeight + vIndex * verticalSpace
                rectBorder.bottom = rectBorder.top + itemHeight
                if (tempWidth >= itemWidth) {
                    // 距离两边的距离
                    val margin = (width - (itemWidth * hNum + (hNum - 1) * horizontalSpace)) / 2
                    rectBorder.left = margin + hIndex * itemWidth + hIndex * horizontalSpace
                    rectBorder.right = itemWidth + rectBorder.left
                } else {
                    rectBorder.left = hIndex * tempWidth + hIndex * horizontalSpace
                    rectBorder.right = tempWidth + rectBorder.left
                }
                drawContent(canvas, rectBorder, str)
                drawLinkLine(canvas, rectBorder, hNum, vIndex)
                list.add(rectBorder)
            }
            rects[vIndex] = list
        }
    }

    /**
     * 画文字和背景
     */
    private fun drawContent(canvas: Canvas, rect: Rect, str: String) {
        canvas.drawRect(rect, paint)
        canvas.drawText(
            str,
            (rect.right + rect.left) / 2f,
            (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.fontMetrics.descent,
            paint
        )
    }

    /***
     * 画连接线
     * @param rect     当前 Item 的 Rect
     * @param currHNum 当前的水平方向 有多少个 Item
     * @param vIndex 当前是垂直方向的第几个
     */
    private fun drawLinkLine(canvas: Canvas, rect: Rect, currHNum: Int, vIndex: Int) {
        val stopX = getX(rect)
        val startX = getX(rect)
        if (vIndex < verticalNum - 1) {
            // 画 Item 下面一半的垂直连接线
            canvas.drawLine(
                startX,
                rect.bottom.toFloat(), stopX, rect.bottom + verticalSpace / 2f, paint
            )

            // 画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect?.top == rect.top) {
                canvas.drawLine(
                    getX(lastItemRect!!),
                    rect.bottom + verticalSpace / 2f, getX(rect),
                    rect.bottom + verticalSpace / 2f, paint
                )
            }
        }
        if (vIndex != 0) {
            // 画 Item 上面一半的垂直连接线
            canvas.drawLine(startX, rect.top - verticalSpace / 2f, stopX, rect.top.toFloat(), paint)

            // 画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect?.top == rect.top) {
                canvas.drawLine(
                    getX(lastItemRect!!),
                    rect.top - verticalSpace / 2f, getX(rect), rect.top - verticalSpace / 2f, paint
                )
            }
        }
        lastItemRect = rect
    }

    private fun getX(rect: Rect): Float {
        return ((rect.left + rect.right) / 2f)
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
                        texts[result.vPosition]!![result.hPosition]
                    )
                }
            }
        }
        return true
    }

    /**
     * 判断点击事件是否在某个上面
     */
    private fun isTouchPointInView(event: MotionEvent): Result {
        if (rects.size == 0) return Result(false, 0, 0)
        val eventX = event.rawX.toInt()
        val eventY = event.y.toInt()
        for (vIndex in 0 until rects.size) {
            val list: List<Rect>? = rects[vIndex]
            for (hIndex in list!!.indices) {
                val rect: Rect = list[hIndex]
                if (rect.contains(eventX, eventY)) {
                    return Result(true, vIndex, hIndex)
                }
            }
        }
        return Result(false, 0, 0)
    }

    fun getTextShow(): HashMap<Int, List<String>> {
        val text1: List<String> = listOf("执行通知")
        val text2: List<String> = listOf("送达文书")
        val text3: List<String> = listOf("强行措施", "财产调查", "解除措施")
        val text4: List<String> = listOf("查询存款", "搜查", "传唤", "悬赏执行")
        val text5: List<String> = listOf("查明财产")
        val text6: List<String> = listOf("查封", "扣押", "冻结", "扣划", "评估", "拍卖")
        val text7: List<String> = listOf("执行和解", "终本约谈", "自动履行")
        val map = HashMap<Int, List<String>>()
        map[0] = text1
        map[1] = text2
        map[2] = text3
        map[3] = text4
        map[4] = text5
        map[5] = text6
        map[6] = text7
        return map
    }


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(vPosition: Int, hPosition: Int, text: String?)
    }

    internal class Result(var isTouchInView: Boolean, var vPosition: Int, var hPosition: Int)
}