package com.simplation.android_banner.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.simplation.android_banner.utils.ScreenUtil;

/**
 * adapter中调用onCreateViewHolder, onBindViewHolder
 */
public class PageAdapterHelper {

    public static int sPagePadding = 0;
    public static int sShowLeftCardWidth = 0;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = parent.getWidth() - ScreenUtil.dip2px(itemView.getContext(), 2 * (sPagePadding + sShowLeftCardWidth));
        itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
        int padding = ScreenUtil.dip2px(itemView.getContext(), sPagePadding);
        itemView.setPadding(padding, 0, padding, 0);
        int leftMarin = position == 0 ? padding + ScreenUtil.dip2px(itemView.getContext(), sShowLeftCardWidth) : 0;
        int rightMarin = position == itemCount - 1 ? padding + ScreenUtil.dip2px(itemView.getContext(), sShowLeftCardWidth) : 0;
        setViewMargin(itemView, leftMarin, rightMarin);
    }

    private void setViewMargin(View view, int left, int right) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != 0 || lp.rightMargin != right || lp.bottomMargin != 0) {
            lp.setMargins(left, 0, right, 0);
            view.setLayoutParams(lp);
        }
    }

    public void setPagePadding(int pagePadding) {
        sPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        sShowLeftCardWidth = showLeftCardWidth;
    }
}
