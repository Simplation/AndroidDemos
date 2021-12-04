package com.simplation.androiddemos.function_summary.banner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.simplation.android_banner.holder.ViewHolderCreator
import com.simplation.android_banner.listener.OnItemClickListener
import com.simplation.androiddemos.R
import kotlinx.android.synthetic.main.activity_banner.*
import java.lang.Exception
import java.util.ArrayList

class BannerActivity : AppCompatActivity(), OnItemClickListener {

    private val localImages = ArrayList<Int>()
    private val networkImages: List<String>? = null
    private val images =
        arrayOf("http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
        )

    private val listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        title = intent.getStringExtra("name")

        initBanner()
    }

    private fun initBanner() {
        //        initImageLoader();
        loadTestData()

        //本地图片例子
        convenientBanner.setPages(
            object : ViewHolderCreator {
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
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT) //设置指示器的方向
        //.setOnPageChangeListener(this)//监听翻页事件
        // convenientBanner.setManualPageable(false);//设置不能手动影响

        //网络加载例子
        /*networkImages = Arrays.asList(images);
        convenientBanner.setPages(new ViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages);*/

        //手动New并且添加到ListView Header的例子
        /*ConvenientBanner mConvenientBanner = new ConvenientBanner(this, false);
        mConvenientBanner.setMinimumHeight(500);
        mConvenientBanner.setPages(
                new ViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                        R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(this);
        listView.addHeaderView(mConvenientBanner);*/
    }

    // 加载测试数据
    private fun loadTestData() {
        //本地图片集合
        for (position in 0..3) localImages.add(getResId("ic_test_$position",
            R.drawable::class.java))


        //各种翻页效果
        /*transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        transformerArrayAdapter.notifyDataSetChanged();*/
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     */
    fun getResId(variableName: String?, c: Class<*>): Int {
        return try {
            val idField = c.getDeclaredField(variableName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }


    override fun onResume() {
        super.onResume()
        //开始自动翻页
        convenientBanner.startTurning()
    }


    override fun onPause() {
        super.onPause()
        //停止翻页
        convenientBanner.stopTurning()
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "点击了第" + position + "个", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HeaderActivity::class.java))
//        convenientBanner.setCanLoop(!convenientBanner.isCanLoop());
    }
}