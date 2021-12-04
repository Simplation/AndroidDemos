package com.simplation.androiddemos.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.simplation.androiddemos.R

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2021-01-18 15:56
 * @描述: 带有清除按钮的 EditText
 * @更新:
 */
class ClearEditText : AppCompatEditText, OnTouchListener, OnFocusChangeListener, TextWatcher {
    private lateinit var mClearIcon: Drawable
    private var mOnTouchListener: OnTouchListener? = null
    private var mOnFocusChangeListener: OnFocusChangeListener? = null

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_input_del)
        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
        mClearIcon = wrappedDrawable
        mClearIcon.setBounds(0, 0, mClearIcon.intrinsicWidth, mClearIcon.intrinsicHeight)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
        ViewCompat.setBackgroundTintList(this,
            ContextCompat.getColorStateList(context, R.color.black60))
    }

    private fun setClearIconVisible(visible: Boolean) {
        if (mClearIcon.isVisible == visible) return
        mClearIcon.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            if (visible) mClearIcon else null,
            compoundDrawables[3])
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    /**
     * [View.OnFocusChangeListener]
     */
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            text?.isNotEmpty()?.let { setClearIconVisible(it) }
        } else {
            setClearIconVisible(false)
        }
        mOnFocusChangeListener?.onFocusChange(view, hasFocus)
    }

    /**
     * [View.OnTouchListener]
     */
    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearIcon.isVisible && x > width - paddingRight - mClearIcon.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mOnTouchListener?.onTouch(view, motionEvent) == true
    }

    /**
     * [TextWatcher]
     */
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}