package com.simplation.android_banner.listener;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @作者: Simplation
 * @日期: 2019/3/15
 * @描述:
 */
public interface OnPageChangeListener {

    /**
     * 滑动状态更改
     *
     * @param recyclerView recyclerView
     * @param newState     state
     */
    void onScrollStateChanged(RecyclerView recyclerView, int newState);

    /**
     * 滑动中
     *
     * @param recyclerView recyclerView
     * @param dx           x的滑动距离
     * @param dy           y的滑动距离
     */
    void onScrolled(RecyclerView recyclerView, int dx, int dy);

    /**
     * 选中状态
     *
     * @param index 下标
     */
    void onPageSelected(int index);
}
