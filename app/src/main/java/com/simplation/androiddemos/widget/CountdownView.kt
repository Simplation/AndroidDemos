package com.simplation.androiddemos.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView


class CountdownView : androidx.appcompat.widget.AppCompatTextView, Runnable {
    private var mTotalTime = 60 // 倒计时秒数
    // 当前秒数
    private var mCurrentTime = 0
    // 记录原有的文本
    private var mRecordText: CharSequence? = null
    // 标记是否重置了倒计控件
    private var mFlag = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    /**
     * 设置倒计时总秒数
     */
    fun setTotalTime(totalTime: Int) {
        mTotalTime = totalTime
    }

    /**
     * 重置倒计时控件
     */
    fun resetState() {
        mFlag = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //设置点击的属性
        isClickable = true
    }

    override fun onDetachedFromWindow() {
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this)
        super.onDetachedFromWindow()
    }

    override fun performClick(): Boolean {
        val click = super.performClick()
        mRecordText = text
        isEnabled = false
        mCurrentTime = mTotalTime
        post(this)
        return click
    }

    /**
     * [Runnable]
     */
    override fun run() {
        if (mCurrentTime == 0 || mFlag) {
            text = mRecordText
            isEnabled = true
            mFlag = false
        } else {
            mCurrentTime--
            text = mCurrentTime.toString() + "\t" + TIME_UNIT
            postDelayed(this, 1000)
        }
    }

    companion object {
        private const val TIME_UNIT = "S" // 秒数单位文本
    }
}