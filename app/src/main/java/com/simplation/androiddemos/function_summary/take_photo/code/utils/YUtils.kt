package com.simplation.androiddemos.function_summary.take_photo.code.utils

import android.Manifest
import android.app.Activity
import android.os.Build
import com.simplation.androiddemos.function_summary.take_photo.code.callback.BaseCallback
import com.simplation.androiddemos.function_summary.take_photo.code.callback.PermissionCallback
import com.simplation.androiddemos.function_summary.take_photo.code.config.Config
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeType
import com.simplation.androiddemos.function_summary.take_photo.code.config.ZoomConfig
import com.simplation.androiddemos.function_summary.take_photo.code.config.ZoomConfigBuilder
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeVideoConfig
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeVideoConfigBuilder
import com.simplation.androiddemos.function_summary.take_photo.code.ui.PermissionActivity
import com.simplation.androiddemos.function_summary.take_photo.code.utils.PhotoUtils.startPick
import com.simplation.androiddemos.function_summary.take_photo.code.utils.PhotoUtils.startRecord


object YUtils {
    fun startTakeVideo(activity: Activity, callBack: BaseCallback) {
        startTakeVideo(activity, null, callBack)
    }

    fun startTakeVideo(
        activity: Activity,
        config: TakeVideoConfig?,
        callBack: BaseCallback,
    ) {
        var config = config
        if (config == null) {
            config = TakeVideoConfigBuilder().createTakeVideoConfig()
        }
        startRecord(activity, config, callBack)
    }

    private fun startTake(activity: Activity, config: Config, callBack: BaseCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 获取运行时权限
            PermissionActivity.start(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : PermissionCallback {
                    override fun onPermissionSuccess() {
                        PhotoUtils.startTake(activity, config, callBack)
                    }

                    override fun onPermissionFailure(toTypedArray: Array<String?>) {
                        callBack.onFailure(0)
                    }
                }
            )
        } else {
            PhotoUtils.startTake(activity, config, callBack)
        }
    }

    fun startForTakePhoto(activity: Activity, callBack: BaseCallback) {
        startTake(activity, Config(false, null, TakeType.TAKE_CAMERA_PHOTO, null), callBack)
    }

    fun startForTakePhotoAndZoom(activity: Activity, config: ZoomConfig?, callBack: BaseCallback) {
        var config = config
        if (config == null) {
            config = ZoomConfigBuilder().createZoomConfig()
        }
        startTake(activity, Config(false, config, TakeType.TAKE_CAMERA_PHOTO, null), callBack)
    }

    fun startForPickGalleryPhotoAndZoom(activity: Activity, callBack: BaseCallback) {
        startForPickGalleryPhotoAndZoom(activity, null, callBack)
    }

    fun startForPickGalleryPhotoAndZoom(
        activity: Activity,
        config: ZoomConfig?,
        callBack: BaseCallback,
    ) {
        var config = config
        if (config == null) {
            config = ZoomConfigBuilder().createZoomConfig()
        }
        start(activity, Config(true, config, TakeType.PICK_GALLERY_PHOTO, null), callBack)
    }

    fun startForPickGalleryPhoto(activity: Activity, callBack: BaseCallback) {
        start(activity, Config(false, null, TakeType.PICK_GALLERY_PHOTO, null), callBack)
    }

    fun startForPickGalleryVideo(activity: Activity, callBack: BaseCallback) {
        start(activity, Config(false, null, TakeType.PICK_GALLERY_VIDEO, null), callBack)
    }

    fun startForPickGalleryPhotoVideo(activity: Activity, callBack: BaseCallback) {
        start(activity, Config(false, null, TakeType.PICK_GALLERY_PHOTO_VIDEO, null), callBack)
    }

    private fun start(activity: Activity, config: Config, callBack: BaseCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 获取运行时权限
            PermissionActivity.start(activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                object : PermissionCallback {
                    override fun onPermissionSuccess() {
                        startPick(activity, config, callBack)
                    }

                    override fun onPermissionFailure(toTypedArray: Array<String?>) {
                        callBack.onFailure(0)
                    }

                })
        } else {
            startPick(activity, config, callBack)
        }
    }
}