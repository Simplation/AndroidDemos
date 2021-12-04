package com.simplation.androiddemos.function_summary.take_photo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.simplation.androiddemos.R
import com.simplation.androiddemos.function_summary.take_photo.code.callback.BaseCallback
import com.simplation.androiddemos.function_summary.take_photo.code.config.TakeVideoConfigBuilder
import com.simplation.androiddemos.function_summary.take_photo.code.config.ZoomConfigBuilder
import com.simplation.androiddemos.function_summary.take_photo.code.utils.YUtils
import com.simplation.androiddemos.function_summary.take_photo.code.utils.YUtils.startForTakePhotoAndZoom
import kotlinx.android.synthetic.main.activity_take_photo.*
import kotlinx.android.synthetic.main.fragment_general.btn_take_photo
import java.io.File


/**
 * Take photo activity
 *
 * @constructor Create empty Take photo activity
 */
class TakePhotoActivity : AppCompatActivity() {

    private val callBack: BaseCallback = object : BaseCallback() {
        override fun onSuccess(path: String) {
            Log.d("yedona", "onSuccess: $path")
            Glide.with(this@TakePhotoActivity).load(File(path)).into(ivTakePhoto)
            Log.d("take photo file path", path)
        }

        override fun onFailure(errorCode: Int) {
            Log.d("yedona", "onFailure: $errorCode")
        }

        override fun onZoomPhotoSuccess(path: String, zoomPath: String) {
            super.onZoomPhotoSuccess(path, zoomPath)
            Log.d("take photo file path", path)
            Log.d("take photo", "onZoomPhotoSuccess: $path\n$zoomPath")
            Glide.with(this@TakePhotoActivity).load(zoomPath).into(ivTakePhoto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        btn_select_picture.setOnClickListener {
            YUtils.startForPickGalleryPhoto(this, callBack)
        }

        btn_select_video.setOnClickListener {
            YUtils.startForPickGalleryVideo(this, callBack)
        }

        btn_select_picture_video.setOnClickListener {
            YUtils.startForPickGalleryPhotoVideo(this, callBack)
        }

        btn_select_picture_edit.setOnClickListener {
            YUtils.startForPickGalleryPhotoAndZoom(this, callBack)
        }

        btn_select_picture_edit_params.setOnClickListener {
            val outPutPath =
                getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath + "/PickPhoto/" + System.currentTimeMillis() + ".jpg"

            val zoomConfig = ZoomConfigBuilder().setAspectX(1)
                .setAspectY(1)
                .setOutputX(200)
                .setOutputY(200)
                .setOutputPath(outPutPath).createZoomConfig()
            YUtils.startForPickGalleryPhotoAndZoom(this, zoomConfig, callBack)
        }

        btn_take_photo_crop.setOnClickListener {
            val outPutPath =
                getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath + "/PickPhoto/" + System.currentTimeMillis() + ".jpg"

            val zoomConfig = ZoomConfigBuilder().setAspectX(1)
                .setAspectY(1)
                .setOutputX(200)
                .setOutputY(200)
                .setOutputPath(outPutPath).createZoomConfig()
            startForTakePhotoAndZoom(this, zoomConfig, callBack)
        }

        btn_take_photo.setOnClickListener {
            YUtils.startForTakePhoto(this, callBack);
        }

        btn_take_video.setOnClickListener {
            YUtils.startTakeVideo(this, callBack);
        }

        btn_take_video_params.setOnClickListener {
            val outPutPath = (getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.absolutePath
                    + "/PickPhoto/" + System.currentTimeMillis() + ".mp4")

            YUtils.startTakeVideo(this,
                TakeVideoConfigBuilder()
                    .setLength(5)
                    .setQuality(1f)
                    .setVideoPath(outPutPath).createTakeVideoConfig(),
                callBack)
        }
    }
}