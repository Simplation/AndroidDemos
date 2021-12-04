package com.simplation.androiddemos.function_summary.hand_write

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.hand_write.signature.SignatureView
import kotlinx.android.synthetic.main.activity_signature_test.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class SignatureTestActivity : BaseActivity() {

    private var createTime: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_signature_test
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        getPermission()
        getCurranTime()

        // 清空
        clear_button.setOnClickListener {
            signature_pad.clear()
        }

        // 保存
        save_button.setOnClickListener {
            val signatureBitmap: Bitmap = signature_pad.getSignatureBitmap()
            val bitmap: Bitmap = compressScale(signatureBitmap)
            if (addSignatureToGallery(bitmap)) {
                Toast.makeText(this@SignatureTestActivity, "保存成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@SignatureTestActivity, "保存失败", Toast.LENGTH_SHORT).show()
            }
        }

        // 绘制面板
        signature_pad.setOnSignedListener(object : SignatureView.OnSignedListener {
            override fun onSigned() {
                save_button.isEnabled =true
                clear_button.isEnabled = true
            }

            override fun onClear() {
                save_button.isEnabled = false
                clear_button.isEnabled = false
            }
        })
    }

    private fun getCurranTime() {
        val format = SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA)
        // 获取当前时间
        val curDate = Date(System.currentTimeMillis())
        createTime = format.format(curDate)
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(
                    this@SignatureTestActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@SignatureTestActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    4
                )
            } else {
                Toast.makeText(this, "已开启权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun String.getAlbumStorageDir(): File? {
        val file = File(Environment.getExternalStorageDirectory(), this)
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File) {
        val newBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    fun addSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            val photo = File(
                "draw".getAlbumStorageDir(),
                String.format("$createTime.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature, photo)
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(photo)
            mediaScanIntent.data = contentUri
            this@SignatureTestActivity.sendBroadcast(mediaScanIntent)
            val intent = intent
            // 这里使用bundle绷带来传输数据
            val bundle = Bundle()
            // 传输的内容仍然是键值对的形式
            bundle.putSerializable("photo", photo)
            intent.putExtras(bundle)
            setResult(RESULT_OK, intent)
            // 需要回调传递的时候，要重写 onActivityResult 回调方法, 添加以下代码

            /*if (requestCode == 2 && resultCode == RESULT_OK) {
                Bundle bundle = data . getExtras ();
                if (bundle != null) {
                    path = (File) bundle . getSerializable ("photo");
                    // 设置为已签字
                    qiyefzerenqz.setText("已设置签名");
                }
            }*/
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    fun compressScale(image: Bitmap): Bitmap {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        if (baos.toByteArray().size / 1024 > 1024) { // 判断如果图片大于 1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset() // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos) //这里压缩50%，把压缩后的数据存放到baos中
        }
        var isBm = ByteArrayInputStream(baos.toByteArray())
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        val hh = 800f // 这里设置高度为800f
        val ww = 480f // 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1 // be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww).toInt()
        } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
            be = (newOpts.outHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        newOpts.inSampleSize = be //设置缩放比例
        // newOpts.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回false了
        isBm = ByteArrayInputStream(baos.toByteArray())
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
        // return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        return bitmap!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<out String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            4 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已打开权限！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "请打开权限！", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }


    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        /**
         * 设置为横屏
         */
        /*if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }*/

        /**
         * 设置为竖屏
         */
        if (requestedOrientation !== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
