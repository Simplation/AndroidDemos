package com.simplation.android_banner.holder;

import android.view.View;

public interface ViewHolderCreator {

    // 创建 Holder
    Holder createHolder(View itemView);

    // 获取布局 ID
    int getLayoutId();
}
