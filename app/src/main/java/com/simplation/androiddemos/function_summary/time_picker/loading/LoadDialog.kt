package com.simplation.androiddemos.function_summary.time_picker.loading

import android.app.ProgressDialog
import android.content.Context

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/20
 * @描述:
 * @更新:
 */
class LoadDialog {

    fun getInstance(): LoadDialog? {
        return LoadDialogHolder.instance
    }

    private object LoadDialogHolder {
        var instance = LoadDialog()
    }

    /**
     * 展示加载框
     *
     * @param context context
     * @param msg     加载信息
     */
    fun show(context: Context, msg: String) {
        close()
        createDialog(context, msg)
        if (progressDialog != null && !progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    /**
     * 关闭加载框
     */
    fun close() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    /**
     * progressDialog
     */
    private var progressDialog: ProgressDialog? = null

    /**
     * 创建加载框
     *
     * @param context context
     * @param msg     msg
     */
    private fun createDialog(
        context: Context,
        msg: String
    ) {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCancelable(false)
        // STYLE_HORIZONTAL：进度条
        // STYLE_SPINNER： 加载圆圈
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage(msg)
        progressDialog!!.setOnCancelListener { progressDialog = null }
    }
}