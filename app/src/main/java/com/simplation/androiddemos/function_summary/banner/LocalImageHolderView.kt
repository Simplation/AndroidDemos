package com.simplation.androiddemos.function_summary.banner

import android.view.View
import android.widget.ImageView
import com.simplation.android_banner.holder.Holder
import com.simplation.androiddemos.R

/**
 * @作者: Simplation
 * @日期: 2021/12/03 14:24
 * @描述:
 * @更新:
 */
class LocalImageHolderView(itemView: View?) : Holder<Int?>(itemView) {
    private var imageView: ImageView? = null
    override fun initView(itemView: View) {
        imageView = itemView.findViewById(R.id.ivPost)
    }

    override fun updateUI(data: Int?) {
        imageView!!.setImageResource(data!!)
    }
}