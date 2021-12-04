package com.simplation.androiddemos.ui.fragment

import android.content.Intent
import android.view.View
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseFragment
import com.simplation.androiddemos.function_summary.baidu_ai.BaiduAIActivity
import com.simplation.androiddemos.function_summary.baidu_track.EagleTrackActivity
import com.simplation.androiddemos.function_summary.ifkey_sdk.IfkytekSdkActivity
import com.simplation.androiddemos.function_summary.picture.PictureSelectorTestActivity
import com.simplation.androiddemos.function_summary.video_player.VideoPlayerActivity
import kotlinx.android.synthetic.main.fragment_third.*

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/18
 * @描述:
 * @更新:
 */
class ThirdFragment : BaseFragment() {

    companion object {
        fun getInstance(): ThirdFragment =
            ThirdFragment()
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_third
    }

    override fun initView(view: View) {
        // 图片选择器
        btn_picture_selector.setOnClickListener {
            Intent(activity, PictureSelectorTestActivity::class.java).run {
                startActivity(this)
            }
        }

        // 科大讯飞 SDK
        btn_iflytek_sdk.setOnClickListener {
            Intent(activity, IfkytekSdkActivity::class.java).run {
                startActivity(this)
            }
        }

        // 百度 AI 实践
        btn_baidu_ai.setOnClickListener {
            Intent(activity, BaiduAIActivity::class.java).run {
                startActivity(this)
            }
        }

        // 饺子视频播放
        btn_video_player.setOnClickListener {
            Intent(activity, VideoPlayerActivity::class.java).run {
                startActivity(this)
            }
        }

        // 百度鹰眼轨迹
        btn_eagle_track.setOnClickListener {
            Intent(activity, EagleTrackActivity::class.java).run {
                startActivity(this)
            }
        }
    }

    override fun lazyLoad() {
    }
}