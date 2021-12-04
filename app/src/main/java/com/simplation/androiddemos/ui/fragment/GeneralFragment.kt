package com.simplation.androiddemos.ui.fragment

import android.content.Intent
import android.view.View
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseFragment
import com.simplation.androiddemos.function_summary.banner.BannerActivity
import com.simplation.androiddemos.function_summary.basic_controler.BasicControlsActivity
import com.simplation.androiddemos.function_summary.charts.ChartsActivity
import com.simplation.androiddemos.function_summary.hand_write.SignatureTestActivity
import com.simplation.androiddemos.function_summary.imitate_fish.ImitateFishActivity
import com.simplation.androiddemos.function_summary.pdf_generate.PdfGenerateTestActivity
import com.simplation.androiddemos.function_summary.process_node.ProcessNodeActivity
import com.simplation.androiddemos.function_summary.single_multiple.RecyclerTestActivity
import com.simplation.androiddemos.function_summary.take_photo.TakePhotoActivity
import com.simplation.androiddemos.function_summary.time_picker.TimePickerTestActivity
import com.simplation.androiddemos.function_summary.token.TokenTestActivity
import com.simplation.androiddemos.function_summary.test.PreferenceScreenActivity
import kotlinx.android.synthetic.main.fragment_general.*

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/18
 * @描述:
 * @更新:
 */
class GeneralFragment : BaseFragment() {

    companion object {
        fun getInstance(): GeneralFragment = GeneralFragment()
    }

    override fun attachLayoutRes(): Int {
        return R.layout.fragment_general
    }

    override fun initView(view: View) {
        // 基础控件
        btn_basic_controls.setOnClickListener {
            Intent(activity, BasicControlsActivity::class.java).run {
                startActivity(this)
            }
        }

        // token_test
        btn_token_test.setOnClickListener {
            Intent(activity, TokenTestActivity::class.java).run {
                //putExtra("title", "Token Test")
                startActivity(this)
            }
        }

        // recycler_view_single_multiple
        btn_single_multiple.setOnClickListener {
            Intent(activity, RecyclerTestActivity::class.java).run {
                //putExtra("title", "Single Multiple")
                startActivity(this)
            }
        }

        // dialog
        btn_dialog.setOnClickListener {
            Intent(activity, TimePickerTestActivity::class.java).run {
                startActivity(this)
            }
        }

        // hand_write
        btn_signature.setOnClickListener {
            Intent(activity, SignatureTestActivity::class.java).run {
                startActivity(this)
            }
        }

        // pdf_generate
        btn_pdf_generate.setOnClickListener {
            Intent(activity, PdfGenerateTestActivity::class.java).run {
                startActivity(this)
            }
        }

        // 仿闲鱼发布页面
        btn_imitate_fish.setOnClickListener {
            Intent(activity, ImitateFishActivity::class.java).run {
                startActivity(this)
            }
        }

        // 流程节点图
        btn_process_node.setOnClickListener {
            Intent(activity, ProcessNodeActivity::class.java).run {
                startActivity(this)
            }
        }

        // Test
        btn_preference.setOnClickListener {
            Intent(activity, PreferenceScreenActivity::class.java).run {
                startActivity(this)
            }
        }

        // 拍照
        btn_take_photo.setOnClickListener {
            Intent(activity, TakePhotoActivity::class.java).run {
                startActivity(this)
            }
        }

        // HelloCharts 图标展示
        btn_hello_chart.setOnClickListener {
            Intent(activity, ChartsActivity::class.java).run {
                startActivity(this)
            }
        }

        // Banner 效果
        btn_banner.setOnClickListener {
            Intent(activity, BannerActivity::class.java).run {
                startActivity(this)
            }
        }




    }

    override fun lazyLoad() {
    }
}