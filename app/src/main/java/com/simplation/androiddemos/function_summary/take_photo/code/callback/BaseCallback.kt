package com.simplation.androiddemos.function_summary.take_photo.code.callback


abstract class BaseCallback {

    abstract fun onSuccess(path: String)

    abstract fun onFailure(errorCode: Int)

    /**
     * On zoom photo success
     *
     * @param path      原图路径
     * @param zoomPath  裁剪后的图片路径
     */
    open fun onZoomPhotoSuccess(path: String, zoomPath: String) {

    }
}