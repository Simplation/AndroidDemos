package com.simplation.android_banner.listener;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * @作者: Simplation
 * @日期: 2019/3/15
 * @描述: 翻页指示器适配器
 */
public class PageChangeListener implements OnPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private OnPageChangeListener onPageChangeListener;

    public PageChangeListener(ArrayList<ImageView> pointViews, int page_indicatorId[]) {
        this.pointViews = pointViews;
        this.page_indicatorId = page_indicatorId;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (onPageChangeListener != null)
            onPageChangeListener.onScrollStateChanged(recyclerView, newState);
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (onPageChangeListener != null) onPageChangeListener.onScrolled(recyclerView, dx, dy);
    }

    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(index).setImageResource(page_indicatorId[1]);
            if (index != i) {
                pointViews.get(i).setImageResource(page_indicatorId[0]);
            }
        }
        if (onPageChangeListener != null) onPageChangeListener.onPageSelected(index);

    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
