package com.simplation.androiddemos.function_summary.banner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.simplation.android_banner.holder.ViewHolderCreator
import com.simplation.android_banner.listener.OnItemClickListener
import com.simplation.androiddemos.R
import kotlinx.android.synthetic.main.item_covenientbanner_header.*
import kotlinx.android.synthetic.main.item_covenientbanner_header.view.*
import java.util.*

class HeaderActivity : AppCompatActivity(), OnItemClickListener,
    BaseQuickAdapter.OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter
    lateinit var header: View
    var datas = ArrayList<String>()
    private val localImages = ArrayList<Int>()
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    fun init() {
        setContentView(R.layout.activity_header)
        recyclerView = findViewById(R.id.recyclerView)
        for (i in 0..99) datas.add("测试$i")
        adapter = MyAdapter(R.layout.item_header_text, datas)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        header = initHeader()
        adapter.addHeaderView(header)
        adapter.onItemClickListener = this
    }

    private fun initHeader(): View {
        val header = LayoutInflater.from(this).inflate(R.layout.item_covenientbanner_header, null)
        loadTestDatas()

        //本地图片例子
        header.convenientBanner.setPages(object : ViewHolderCreator {
            override fun createHolder(itemView: View?): LocalImageHolderView {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_localimage
            }
        }, localImages as List<Nothing>?) //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            .setPageIndicator(intArrayOf(R.drawable.ic_page_indicator,
                R.drawable.ic_page_indicator_focused))
            .setOnItemClickListener(this)

        return header
    }

    private fun loadTestDatas() {
        //本地图片集合
        for (position in 0..3) localImages.add(getResId("ic_test_$position",
            R.drawable::class.java))
    }

    // 开始自动翻页
    override fun onResume() {
        super.onResume()
        //开始自动翻页
        header.convenientBanner.startTurning()
    }

    // 停止自动翻页
    override fun onPause() {
        super.onPause()
        //停止翻页
        header.convenientBanner.stopTurning()
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "点击了Banner第" + position + "个", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, AlbumActivity::class.java))
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        Toast.makeText(this, "点击了List第" + position + "个", Toast.LENGTH_SHORT).show()
    }

    inner class MyAdapter(layoutResId: Int, data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tvText, item)
        }
    }

    companion object {
        /**
         * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
         *
         * @param variableName
         * @param c
         * @return
         */
        fun getResId(variableName: String, c: Class<*>): Int {
            return try {
                val idField = c.getDeclaredField(variableName)
                idField.getInt(idField)
            } catch (e: Exception) {
                e.printStackTrace()
                -1
            }
        }
    }

}