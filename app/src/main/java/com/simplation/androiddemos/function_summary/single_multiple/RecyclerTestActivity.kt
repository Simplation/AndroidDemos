package com.simplation.androiddemos.function_summary.single_multiple

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.single_multiple.act.MultipleSelectionActivity
import com.simplation.androiddemos.function_summary.single_multiple.act.SingleSelectionActivity
import kotlinx.android.synthetic.main.activity_recycler_test.*

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/19 16:49
 * @描述: RecyclerView 单选和多选
 * @更新:
 */
class RecyclerTestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_recycler_test
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        single_selection.setOnClickListener(onClickListener)
        multiple_selection.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            // 单选
            R.id.single_selection -> {
                Intent(this, SingleSelectionActivity::class.java).run {
                    putExtra("title", "Single Selection")
                    startActivity(this)
                }
            }

            // 多选
            R.id.multiple_selection -> {
                Intent(this, MultipleSelectionActivity::class.java).run {
                    putExtra("title", "Multiple Selection")
                    startActivity(this)
                }
            }
        }
    }
}
