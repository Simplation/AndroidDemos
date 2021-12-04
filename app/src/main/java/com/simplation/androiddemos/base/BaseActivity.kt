package com.simplation.androiddemos.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setView(savedInstanceState)
        initData()
        initView()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun setView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    protected abstract fun initView()

}