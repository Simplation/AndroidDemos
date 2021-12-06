package com.simplation.androiddemos.function_summary.process_node

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.widget.processview.ProcessView
import com.simplation.androiddemos.widget.processview.ProcessView1
import com.simplation.androiddemos.widget.processview.ProcessView2
import kotlinx.android.synthetic.main.activity_process_node.*
import kotlin.collections.HashMap

/**
 * @作者: Simplation
 * @日期: 2020-11-30 11:01
 * @描述: 流程节点图
 *          ProcessView 不需要进行 setData 操作
 *          注意：ProcessView1 和 ProcessView2 需要进行 setData 操作
 * @更新:
 */
class ProcessNodeActivity : BaseActivity(),
    ProcessView.OnItemClickListener,
    ProcessView1.OnItemClickListener,
    ProcessView2.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.activity_process_node

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        process_view.setOnItemClickListener(this)

        // process node2
        process_view.setData(getTextShow(), getMeasureRecord())
    }

    private fun getTextShow(): HashMap<Int, List<String>> {
        val text1: List<String> = mutableListOf("执行通知")
        val text2: List<String> = mutableListOf("送达文书", "调查")
        val text3: List<String> = mutableListOf("强行措施", "财产调查", "解除措施")
        val text4: List<String> = mutableListOf("查询存款", "搜查", "传唤", "悬赏执行")
        val text5: List<String> = mutableListOf("查明财产")
        val text6: List<String> = mutableListOf("查封", "扣押", "冻结", "扣划", "评估", "拍卖")
        val text7: List<String> = mutableListOf("执行和解", "终本约谈", "自动履行")
        val map = HashMap<Int, List<String>>()
        map[0] = text1
        map[1] = text2
        map[2] = text3
        map[3] = text4
        map[4] = text5
        map[5] = text6
        map[6] = text7
        return map
    }


    private fun getMeasureRecord(): HashMap<Int, List<Boolean>> {
        val text1: List<Boolean> = mutableListOf(true)
        val text2: List<Boolean> = mutableListOf(true, false)
        val text3: List<Boolean> = mutableListOf(true, true, true)
        val text4: List<Boolean> = mutableListOf(true, true, true, true)
        val text5: List<Boolean> = mutableListOf(true)
        val text6: List<Boolean> = mutableListOf(true, true, true, true, true, true)
        val text7: List<Boolean> = mutableListOf(false, true, false)
        val map = HashMap<Int, List<Boolean>>()
        map[0] = text1
        map[1] = text2
        map[2] = text3
        map[3] = text4
        map[4] = text5
        map[5] = text6
        map[6] = text7
        return map
    }

    override fun onItemClick(vPosition: Int, hPosition: Int, text: String?) {
        Log.d("manny", "vPosition=$vPosition,hPosition=$hPosition,text= $text")
        Toast.makeText(
            this,
            "vPosition=$vPosition,hPosition=$hPosition,text= $text",
            Toast.LENGTH_LONG
        ).show()
    }


}