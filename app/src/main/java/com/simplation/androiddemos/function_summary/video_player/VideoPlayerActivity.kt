package com.simplation.androiddemos.function_summary.video_player

import android.os.Bundle
import android.widget.TextView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import kotlinx.android.synthetic.main.activity_video_player.*

/**
 * @作者: Simplation
 * @日期: 2020/12/06
 * @描述:
 * @更新:
 */
class VideoPlayerActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_video_player
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        val url: String = url_text.text.toString().trim { it <= ' ' }

        jz_video.setUp(url, "惊奇队长·逆转无限")
        jz_video.thumbImageView.setImageResource(R.mipmap.logo)
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }
}