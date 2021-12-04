package com.simplation.androiddemos.function_summary.ifkey_sdk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.ifkey_sdk.voice.*
import com.simplation.androiddemos.function_summary.ifkey_sdk.voice.faceonline.OnlineFaceActivity
import com.simplation.androiddemos.function_summary.ifkey_sdk.voice.vocalverify.VocalVerifyActivity
import kotlinx.android.synthetic.main.activity_ifkytek_sdk.*


class IfkytekSdkActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_ifkytek_sdk
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        // 语音转写
        btn_transliteration.setOnClickListener(onClickListener)
        // 语法识别
        btn_recognition.setOnClickListener(onClickListener)
        // 语义理解
        btn_understanding.setOnClickListener(onClickListener)
        // 语音合成
        btn_evaluation.setOnClickListener(onClickListener)
        // 语音评测
        btn_synthesis.setOnClickListener(onClickListener)
        // btn_wake_up
        btn_wake_up.setOnClickListener(onClickListener)
        // 声纹
        btn_voice.setOnClickListener(onClickListener)
        // 人脸识别(在线)
        btn_face_recognition.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_transliteration -> {
                Intent(this, IatActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_recognition -> {
                Intent(this, AsrActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_understanding -> {
                Toast.makeText(this, "语义理解按钮被点击...", Toast.LENGTH_SHORT).show()
                /*Intent(this, IatTestActivity::class.java).run {
                    startActivity(this)
                }*/
            }

            R.id.btn_evaluation -> {
                Intent(this, TtsActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_synthesis -> {
                Intent(this, IseActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_wake_up -> {
                Intent(this, IvwActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_voice -> {
                Intent(this, VocalVerifyActivity::class.java).run {
                    startActivity(this)
                }
            }

            R.id.btn_face_recognition -> {
                Intent(this, OnlineFaceActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }
}
