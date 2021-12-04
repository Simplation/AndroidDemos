package com.simplation.androiddemos.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import kotlinx.android.synthetic.main.activity_logo_view.*
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/18 9:53
 * @描述: 启动页
 * @更新:
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private var alphaAnimation: AlphaAnimation? = null

    override fun getLayoutId(): Int {
//        return R.layout.activity_splash
        return R.layout.activity_logo_view
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        /*alphaAnimation = AlphaAnimation(0.3F, 1.0F)
        alphaAnimation?.run {
            duration = 2000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    jumpToMain()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
        layout_splash.startAnimation(alphaAnimation)*/

        anim_logo.addOffsetAnimListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                Log.d("AnimLogoView", "Offset anim start")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d("AnimLogoView", "Offset anim end")
                jumpToMain()
            }
        })
        anim_logo.startAnimation()

    }

    private fun jumpToMain() {
        Intent().apply {
            /**
             * MainActivity 使用传统的方式
             * Main2Activity 推荐使用方式 2
             */
            // setClass(this@SplashActivity, MainActivity::class.java)
            setClass(this@SplashActivity, Main2Activity::class.java)
            startActivity(this)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
