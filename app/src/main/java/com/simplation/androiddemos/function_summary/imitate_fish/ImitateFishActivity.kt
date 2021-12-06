package com.simplation.androiddemos.function_summary.imitate_fish

import android.os.Bundle
import android.widget.Toast
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import kotlinx.android.synthetic.main.activity_imitate_fish.*

/**
 * @作者: Simplation
 * @日期: 2021/12/06 10:10
 * @描述:
 * @更新:
 */
class ImitateFishActivity : BaseActivity() {
    var publishDialog: PublishDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_imitate_fish
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        open_dialog.setOnClickListener {
            if (publishDialog == null) {
                publishDialog = PublishDialog(this@ImitateFishActivity)
                publishDialog!!.setFabuClickListener {
                    Toast.makeText(
                        this@ImitateFishActivity,
                        "发布",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                publishDialog!!.setHuishouClickListener {
                    Toast.makeText(
                        this@ImitateFishActivity,
                        "回收",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                publishDialog!!.setPingguClickListener {
                    Toast.makeText(
                        this@ImitateFishActivity,
                        "评估",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            publishDialog!!.show()
        }
    }
}