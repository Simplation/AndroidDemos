package com.simplation.androiddemos.function_summary.take_photo.code.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import com.blankj.utilcode.util.UriUtils
import com.simplation.androiddemos.function_summary.take_photo.code.bean.AvoidOnResult
import com.simplation.androiddemos.function_summary.take_photo.code.callback.BaseCallback
import com.simplation.androiddemos.function_summary.take_photo.code.config.Config
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeType
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeVideoConfig
import java.io.File
import java.io.IOException


object PhotoUtils {

    fun startPick(activity: Activity, config: Config, callBack: BaseCallback) {
        var uriType: String? = null
        if (config.type === TakeType.PICK_GALLERY_PHOTO_VIDEO) {
            uriType = "video/*,image/*"
        }

        if (config.type === TakeType.PICK_GALLERY_PHOTO) {
            uriType = "image/*"
        }

        if (config.type === TakeType.PICK_GALLERY_VIDEO) {
            uriType = "video/*"
        }

        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, uriType)
        AvoidOnResult(activity)
            .startForResult(pickIntent, object : AvoidOnResult.Callback {
                override fun onActivityResult(resultCode: Int, data: Intent?) {
                    if (resultCode == Activity.RESULT_OK) {
                        val uri: Uri? = data!!.data
                        val imgPath: String = UriUtils.uri2File(uri).absolutePath
                        if (config.type === TakeType.PICK_GALLERY_PHOTO) {
                            PhotoZoomUtils.startForPhotoZoom(activity,
                                config.zoomConfig!!,
                                callBack,
                                imgPath)
                            callBack.onSuccess(imgPath)
                        } else {
                            callBack.onSuccess(imgPath)
                        }
                    } else {
                        callBack.onFailure(1)
                    }
                }
            })
    }

    fun startTake(activity: Activity, config: Config, callBack: BaseCallback) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File? = activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val path: String =
            file?.absolutePath.toString() + "/" + System.currentTimeMillis() + ".jpg"
        try {
            File(path).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val uri: Uri = UriUtils.file2Uri(File(path))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        AvoidOnResult(activity)
            .startForResult(intent, object : AvoidOnResult.Callback {
                override fun onActivityResult(resultCode: Int, data: Intent?) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (config.type === TakeType.TAKE_CAMERA_PHOTO) {
                            PhotoZoomUtils.startForPhotoZoom(activity,
                                config.zoomConfig!!,
                                callBack,
                                path)
                            callBack.onSuccess(path)
                        } else {
                            callBack.onSuccess(path)
                        }
                    } else {
                        callBack.onFailure(1)
                    }
                }
            })
    }

    fun startRecord(activity: Activity, config: TakeVideoConfig, callBack: BaseCallback) {
        if (TextUtils.isEmpty(config.videoPath)) {
            val file: File? = activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)
            val path: String = file?.absolutePath.toString() + "/" + System.currentTimeMillis() + ".mp4"
            config.videoPath = path
        }
        val file = File(config.videoPath)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val uri: Uri = UriUtils.file2Uri(File(config.videoPath))
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.quility)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.length)
        AvoidOnResult(activity)
            .startForResult(intent, object : AvoidOnResult.Callback {
                override fun onActivityResult(resultCode: Int, data: Intent?) {
                    if (resultCode == Activity.RESULT_OK) {
                        callBack.onSuccess(config.videoPath)
                    } else {
                        callBack.onFailure(1)
                    }
                }
            })
    }
}