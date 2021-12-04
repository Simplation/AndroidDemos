package com.simplation.androiddemos.function_summary.take_photo.code.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.blankj.utilcode.util.UriUtils
import com.simplation.androiddemos.function_summary.take_photo.code.bean.AvoidOnResult
import com.simplation.androiddemos.function_summary.take_photo.code.callback.BaseCallback
import com.simplation.androiddemos.function_summary.take_photo.code.config.ZoomConfig
import java.io.File
import java.io.IOException


object PhotoZoomUtils {
    /**
     * 调用系统裁剪功能
     */
    fun startForPhotoZoom(
        context: Activity,
        config: ZoomConfig,
        callBack: BaseCallback,
        originFilePath: String,
    ) {
        val isOutPut = !TextUtils.isEmpty(config.outputPath)
        var outputUri: Uri? = null
        if (isOutPut) {
            val file = File(config.outputPath)
            if (!file.exists()) {
                try {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            outputUri = Uri.fromFile(File(config.outputPath))
        } else {
            val outputPath = originFilePath.substring(0,
                originFilePath.lastIndexOf(File.separator)) + File.separator + System.currentTimeMillis()
                .toString() + ".jpg"
            outputUri = Uri.fromFile(File(outputPath))
            config.outputPath = outputPath
        }
        val uri: Uri = UriUtils.file2Uri(File(originFilePath))
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", config.aspectX)
        intent.putExtra("aspectY", config.aspectY)
        intent.putExtra("outputX", config.outputX)
        intent.putExtra("outputY", config.outputY)
        intent.putExtra("return-data", true)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        AvoidOnResult(context)
            .startForResult(intent, object : AvoidOnResult.Callback {
                override fun onActivityResult(resultCode: Int, data: Intent?) {
                    val path: String? = if (data == null || data.data == null) {
                        config.outputPath
                    } else {
                        UriUtils.uri2File(data.data).absolutePath
                    }
                    if (path != null) {
                        callBack.onZoomPhotoSuccess(originFilePath, path)
                    }
                }
            })
    }
}