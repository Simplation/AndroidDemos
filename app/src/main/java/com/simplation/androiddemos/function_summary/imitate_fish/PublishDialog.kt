package com.simplation.androiddemos.function_summary.imitate_fish

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.simplation.androiddemos.R
import kotlinx.android.synthetic.main.mian_dialog_publish.*
import java.util.*

/**
 * @作者: Simplation
 * @日期: 2021/12/06 10:13
 * @描述:
 * @更新:
 */
class PublishDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.main_publishdialog_style
) :
    Dialog(context, themeResId) {
    private var mHandler: Handler? = null
    private val mContext: Context = context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏
        val params = Objects.requireNonNull(
            window
        )?.attributes
        if (params != null) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        window!!.attributes = params
        dialog = this
    }

    /**
     * 初始化
     */
    private fun init() {
        setContentView(R.layout.mian_dialog_publish)
        mHandler = Handler()
        publish_dialog_llBt.setOnClickListener { outDia() }
        publish_main_rlmian.setOnClickListener { outDia() }  // 主布局
    }

    override fun show() {
        super.show()
        goinDia()
    }

    /**
     * 进入dialog
     */
    private fun goinDia() {
        Publish_dialog_fabu!!.visibility = View.INVISIBLE
        publish_dialog_huishou!!.visibility = View.INVISIBLE
        publish_dialog_pinggu!!.visibility = View.INVISIBLE
        //首先把发布回收评估三个控件设置为不可见
        publish_main_rlmian!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.main_go_in)
        //然后设置主布局的动画
        publish_dialog_ivMenu!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.main_rotate_right)
        //这里设置底部退出按钮的动画 这里是用了一个rotate动画
        Publish_dialog_fabu!!.visibility = View.VISIBLE
        //底部按钮动画执行过之后把发布设置为可见
        Publish_dialog_fabu!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.mian_shoot_in)
        //然后让他执行mian_shoot_in动画这个动画里定义的是平移动画
        //在这里设置之后如果你同时设置其他两个评估和回收动画着这三个动画会同时从屏幕的底部向上平移
        //而我们想实现的效果是挨个向上平移这里 使用到了定时器handler开启一个线程定时100毫秒启动这个线程
        // 这样就可以达到挨个向上平移的效果
        // mHandler.postDelayed开启一个定时任务
        mHandler!!.postDelayed({
            publish_dialog_huishou!!.visibility = View.VISIBLE
            publish_dialog_huishou!!.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.mian_shoot_in
            )
        }, 100)
        mHandler!!.postDelayed({
            publish_dialog_pinggu!!.visibility = View.VISIBLE
            publish_dialog_pinggu!!.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.mian_shoot_in
            )
        }, 200)
        //这里需要设置成两百不然会出现和评估同时向上滑动
    }

    /**
     * 退出Dialog
     */
    fun outDia() {
        publish_main_rlmian!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.main_go_out)
        publish_dialog_ivMenu!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.main_rotate_left)
        //设置退出按钮从右向左旋转
        mHandler!!.postDelayed({ dismiss() }, 500)
        //这里设置了一个定时500毫秒的定时器来执行dismiss();来关闭Dialog 我们需要在500毫秒的时间内完成对控件动画的设置
        Publish_dialog_fabu!!.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.mian_shoot_out)
        //然后设置发布从上向下平移动画
        Publish_dialog_fabu!!.visibility = View.INVISIBLE
        //将其设置为不可见
        mHandler!!.postDelayed({
            publish_dialog_huishou!!.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.mian_shoot_out
            )
            publish_dialog_huishou!!.visibility = View.INVISIBLE
        }, 100)
        //同理使用定时器将评估和回向下平移 这里需要注意的是评估和回收的定时器时间的设置不能大于关闭Dialog的定时时间
        mHandler!!.postDelayed({
            publish_dialog_pinggu!!.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.mian_shoot_out
            )
            publish_dialog_pinggu!!.visibility = View.INVISIBLE
        }, 150)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (isShowing) {
            outDia()
            //这里重写了onKeyDown方法捕获了back键的执行事件 点击back将退出Dialog
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    //这三个方法设置了三个控件的点击事件并返回一个PublishDialog 这里需要一个OnClickListener的参数
    fun setFabuClickListener(clickListener: View.OnClickListener?): PublishDialog {
        Publish_dialog_fabu!!.setOnClickListener(clickListener)
        return this
    }

    fun setHuishouClickListener(clickListener: View.OnClickListener?): PublishDialog {
        publish_dialog_huishou!!.setOnClickListener(clickListener)
        return this
    }

    fun setPingguClickListener(clickListener: View.OnClickListener?): PublishDialog {
        publish_dialog_pinggu!!.setOnClickListener(clickListener)
        return this
    }

    companion object {
        var dialog: Dialog? = null
    }

    init {
        init()
    }
}