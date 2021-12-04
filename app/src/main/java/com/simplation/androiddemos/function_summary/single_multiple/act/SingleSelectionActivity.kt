package com.simplation.androiddemos.function_summary.single_multiple.act

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.single_multiple.adapter.SingleAdapter
import kotlinx.android.synthetic.main.activity_single_selection.*

class SingleSelectionActivity : BaseActivity() {

    private val datas = mutableListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_single_selection
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        // 创建模拟数据
        createData()
    }

    private fun createData() {
        for (i in 0..19) {
            datas.add("测试单选数据$i")
        }
    }

    override fun initView() {
        title = intent.getStringExtra("title")

        single_recyclerview.layoutManager = LinearLayoutManager(this)
        val singleAdapter = SingleAdapter(this, datas)
        single_recyclerview.adapter = singleAdapter

        singleAdapter.setOnItemClickLitener(object : SingleAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                singleAdapter.setSelection(position)
            }

        })
    }

}
