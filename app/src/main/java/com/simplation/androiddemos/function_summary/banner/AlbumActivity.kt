package com.simplation.androiddemos.function_summary.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.simplation.android_banner.holder.ViewHolderCreator
import com.simplation.androiddemos.R
import kotlinx.android.synthetic.main.activity_album.*
import me.relex.photodraweeview.OnViewTapListener
import java.util.*

class AlbumActivity : AppCompatActivity(), OnViewTapListener {
    private val images = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //下面fresco初始化偷懒写在这了
        Fresco.initialize(applicationContext)
        setContentView(R.layout.activity_album)
        init()
    }

    private fun init() {
        loadTestDatas()
        //图片例子
        convenientBanner.setPages(
            object : ViewHolderCreator {
                override fun createHolder(itemView: View?): NetWorkImageHolderView {
                    return NetWorkImageHolderView(itemView, this@AlbumActivity)
                }

                override fun getLayoutId(): Int {
                    return R.layout.item_photoview
                }

            }, images as List<Nothing>?) //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            .setPageIndicator(intArrayOf(R.drawable.ic_page_indicator,
                R.drawable.ic_page_indicator_focused))
    }

    /*
    加入测试Views
    * */
    private fun loadTestDatas() {
        //图片可能过期哦，自己换来测试吧
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525319864&di=87f476652c96678547ccabbf112076be&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fgamephotolib%2F1410%2F27%2Fc0%2F40170771_1414341013392.jpg")
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524714908870&di=9d43d35cefbabacdc879733aa7ddc82b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D46de93bfc711728b24208461a095a9bb%2F4610b912c8fcc3ce5423d51d9845d688d43f2038.jpg")
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524714935901&di=052557513540f3d740eeeb2439c585bb&imgtype=0&src=http%3A%2F%2Fwww.gzlco.com%2Fimggzl%2F214%2F1b6e6520ca474fe4bd3ff728817950717651.jpeg")
    }

    override fun onViewTap(view: View, x: Float, y: Float) {
        //单击关闭
        finish()
    }
}