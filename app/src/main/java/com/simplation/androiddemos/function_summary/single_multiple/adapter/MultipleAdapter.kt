package com.simplation.androiddemos.function_summary.single_multiple.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simplation.androiddemos.R
import com.simplation.androiddemos.function_summary.single_multiple.bean.Bean

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/19
 * @描述:
 * @更新:
 */
class MultipleAdapter(context: Context, private var datas: List<Bean>) :
    RecyclerView.Adapter<MultipleAdapter.MultipleViewHolder>() {

    val data = mutableListOf<Bean>()
    var isSelected = hashMapOf<Int, Boolean>()

    var onClick: OnItemClickListener? = null

    fun setOnItemClickLitener(mOnItemClickLitener: OnItemClickListener?) {
        this.onClick = mOnItemClickLitener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MultipleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MultipleViewHolder, position: Int) {
        val viewHolder: MultipleViewHolder = holder
        val bean = datas[position]
        viewHolder.mTvName.text = bean.title
        viewHolder.mCheckBox.isChecked = bean.isSelect
        viewHolder.itemView.isSelected = bean.isSelect
        if (onClick != null) viewHolder.itemView.setOnClickListener {
            bean.isSelect = !bean.isSelect
            notifyDataSetChanged()
            onClick!!.onItemClick(bean)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(bean: Bean)
    }

    inner class MultipleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTvName: TextView = itemView.findViewById(R.id.tv_name)
        var mCheckBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }
}