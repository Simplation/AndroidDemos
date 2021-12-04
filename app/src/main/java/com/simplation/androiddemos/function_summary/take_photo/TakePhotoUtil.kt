package com.simplation.androiddemos.function_summary.take_photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object TakePhotoUtil {

    private lateinit var image: File
    private var imgPath: String = ""

    @SuppressLint("QueryPermissionsNeeded")
    fun dispatchTakePictureIntent(activity: Activity) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
        ) {
            // 拍照方法
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile(activity)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (photoFile != null) {
                    val photoUri: Uri = FileProvider.getUriForFile(activity,
                        activity.packageName + ".simplation", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    activity.startActivityForResult(takePictureIntent, 10001)
                }
            }
        } else {
            // 提示用户开户权限   拍照和读写 sd 卡权限
            val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(activity, perms, 10010)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun dispatchTakePictureIntent(fragment: Fragment) {
        if (PackageManager.PERMISSION_GRANTED == fragment.context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
            }
        ) {
            //  拍照方法
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(fragment.requireActivity().packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile(fragment.requireActivity())
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (photoFile != null) {
                    val photoUri: Uri = FileProvider.getUriForFile(fragment.requireActivity(),
                        fragment.requireActivity().packageName.toString() + ".simplation",
                        photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    // 在 fragment 页面拍照需要使用 fragment 中的 startActivityForResult，不然不会走 fragment 中的回调
                    fragment.startActivityForResult(takePictureIntent, 10001)
                }
            }
        } else {
            // 提示用户开户权限   拍照和读写 sd 卡权限
            val perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            // 在 fragment 中申请权限时候需要使用 fragment 中的 requestPermissions 方法不然不会走 fragment 中的回调
            fragment.requestPermissions(perms, 10010)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(activity: Activity): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        image = File.createTempFile(imageFileName, ".jpg", storageDir)
        imgPath = image.absolutePath
        Log.e("打印图片路径", "path: $imgPath")
        return image
    }

    fun getImgPath():String {
        return image.absolutePath
    }
}