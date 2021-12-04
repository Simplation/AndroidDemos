package com.simplation.androiddemos.function_summary.banner

import android.graphics.drawable.Animatable
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.simplation.android_banner.holder.Holder
import com.simplation.androiddemos.R
import me.relex.photodraweeview.OnViewTapListener
import me.relex.photodraweeview.PhotoDraweeView

/**
 * @作者: Simplation
 * @日期: 2021/12/03 14:28
 * @描述:
 * @更新:
 */
class NetWorkImageHolderView(itemView: View?, onViewTapListener: OnViewTapListener?) :
    Holder<String?>(itemView) {
    private lateinit var ivPost: PhotoDraweeView
    override fun initView(itemView: View) {
        ivPost = itemView.findViewById(R.id.ivPost)
    }

    override fun updateUI(data: String?) {
        val controller: PipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder()
        controller.setUri(data)
        controller.oldController = ivPost.controller
        controller.controllerListener = object : BaseControllerListener<ImageInfo?>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                super.onFinalImageSet(id, imageInfo, animatable)
                if (imageInfo == null) {
                    return
                }
                ivPost.update(imageInfo.width, imageInfo.height)
            }
        }
        ivPost.controller = controller.build()
    }

    init {
        ivPost.onViewTapListener = onViewTapListener
    }
}