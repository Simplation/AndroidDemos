package com.simplation.androiddemos.function_summary.time_picker

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.time_picker.loading.LoadDialog
import com.simplation.androiddemos.function_summary.time_picker.picker.builder.TimePickerBuilder
import com.simplation.androiddemos.function_summary.time_picker.picker.view.TimePickerView
import kotlinx.android.synthetic.main.activity_time_picker_test.*
import java.text.SimpleDateFormat
import java.util.*

class TimePickerTestActivity : BaseActivity() {

    companion object {
        var pickerTime: TimePickerView? = null
        var loadDialog: LoadDialog? = null
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_time_picker_test
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        initTimePicker()

        // 展示时间弹出框
        show_picker.setOnClickListener {
            pickerTime!!.show()
        }

        // 展示弹出框
        show_dialog.setOnClickListener {
            val datas = arrayOf("Java", "Objective-C", "Kotlin", "Dart")

            val levelDialog: AlertDialog =
                AlertDialog.Builder(this)
                    .setIcon(R.mipmap.logo) // 设置标题的图片
                    .setTitle("Dialog Title") // 设置对话框的标题
                    .setSingleChoiceItems(datas, 0) { dialog: DialogInterface, which: Int ->
                        if (which >= 0) {
                            val status = datas[which]
                            Toast.makeText(this, "选中的是$status", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            /*when (status) {
                                "Java" -> {
                                    Log.d("data", "Java")
                                }
                                "Objective-C" -> {
                                    Log.d("data", "Objective-C")
                                }
                                "Kotlin" -> {
                                    Log.d("data", "Kotlin")
                                }
                                else -> {
                                    Log.d("data", "Dart")
                                }
                            }*/
                        }
                    }
                    .setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .create()
            levelDialog.show()
        }

        // 显示加载框
        show_loading.setOnClickListener {
            loadDialog = LoadDialog().getInstance()
            loadDialog?.show(this, "正在加载, 请稍后...")

            // 创建 Thread 睡眠 3s 后关闭弹出框
            val thread = MyThread()
            Thread(thread).start()
        }

        // 显示状态提示框
        show_tip.setOnClickListener {

        }
    }

    private fun initTimePicker() {
        pickerTime = TimePickerBuilder(this) { date, _ ->
            show_picker.text = getTime(date)
        }
            .setTimeSelectChangeListener {
                Log.i("pvTime", "onTimeSelectChanged")
            }
            .setType(booleanArrayOf(true, true, true, true, true, true))
            //.setType(booleanArrayOf(true, true, true, false, false, false))
            .isDialog(true)
            .setTitleText("Time Title")
            .addOnCancelClickListener {
                Log.i("pvTime", "onCancelClickListener")
            }.build()

        val mDialog = pickerTime!!.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pickerTime!!.dialogContainerLayout.layoutParams = params

            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim)
                dialogWindow.setGravity(Gravity.BOTTOM)
                dialogWindow.setDimAmount(0.1f)
            }
        }
    }

    private fun getTime(date: Date?): String? {
        // 可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date!!.time)

        // 格式化年月日时分秒
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // 格式化年月日
        // val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return format.format(date)
    }

    class MyThread : Thread() {
        override fun run() {
            super.run()

            try {
                // 睡眠 3s 模拟关闭弹出框
                sleep(3 * 1000)

                loadDialog!!.getInstance()!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
