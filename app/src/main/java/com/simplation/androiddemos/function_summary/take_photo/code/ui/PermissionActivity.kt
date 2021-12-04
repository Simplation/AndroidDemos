package com.simplation.androiddemos.function_summary.take_photo.code.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.simplation.androiddemos.function_summary.take_photo.code.callback.PermissionCallback


class PermissionActivity : Activity() {

    companion object {
        private var mPermissionCallback: PermissionCallback? = null

        fun start(context: Context, permissions: Array<String>, callback: PermissionCallback) {
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra("permissions", permissions)
            mPermissionCallback = callback
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        window.setGravity(Gravity.LEFT or Gravity.TOP)
        val params = window.attributes
        params.x = 0
        params.y = 0
        params.height = 1
        params.width = 1

        // 兼容低版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        // 当 api 大于 23 时，才进行权限申请
        val permissions = intent.getStringArrayExtra("permissions")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && permissions.isNotEmpty()) {
            requestPermissions(permissions, 0x01)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 处理申请结果
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] =
                shouldShowRequestPermissionRationale(permissions[i])
        }
        this.onRequestPermissionsResult(permissions,
            grantResults,
            shouldShowRequestPermissionRationale)
    }

    @TargetApi(23)
    fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        shouldShowRequestPermissionRationale: BooleanArray,
    ) {
        val length = permissions.size
        var granted = 0
        val mPermissionRejectList: MutableList<String?> = ArrayList()
        for (i in 0 until length) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale[i]) {
                    mPermissionRejectList.add(permissions[i])
                }
            } else {
                granted++
            }
        }
        if (granted == length) {
            mPermissionCallback?.onPermissionSuccess()
        } else {
            mPermissionCallback?.onPermissionFailure(mPermissionRejectList.toTypedArray())
        }
        mPermissionCallback = null
        finish()
    }
}