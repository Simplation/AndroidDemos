package com.simplation.androiddemos.function_summary.single_multiple.act

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.single_multiple.adapter.MultipleAdapter
import com.simplation.androiddemos.function_summary.single_multiple.bean.Bean
import kotlinx.android.synthetic.main.activity_multiple_selection.*

class MultipleSelectionActivity : BaseActivity() {

    var datas = mutableListOf<Bean>()
    val selectDatas = mutableListOf<Bean>()
    private var multipleAdapter: MultipleAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_multiple_selection
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        initDatas()
    }

    override fun initView() {
        title = intent.getStringExtra("title")

        multiple_recycler.setHasFixedSize(true)
        multiple_recycler.layoutManager = LinearLayoutManager(this)
        multipleAdapter = MultipleAdapter(this, datas)
        multiple_recycler.adapter = multipleAdapter


        multipleAdapter!!.setOnItemClickLitener(object : MultipleAdapter.OnItemClickListener {
            override fun onItemClick(bean: Bean) {
                if (bean.isSelect) {
                    selectDatas.add(bean)
                } else {
                    selectDatas.remove(bean)
                }
                tv_count.text = String.format("已选中 %s 项", selectDatas.size)
            }

        })
    }

    private fun initDatas() {
        datas = ArrayList()

        for (i in 0..19) {
            datas.add(
                Bean(
                    "测试$i"
                )
            )
        }
    }
}
