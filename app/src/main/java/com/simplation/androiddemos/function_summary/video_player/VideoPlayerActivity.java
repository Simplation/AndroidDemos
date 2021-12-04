package com.simplation.androiddemos.function_summary.video_player;

import android.os.Bundle;
import android.widget.TextView;

import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayerActivity extends BaseActivity {

    TextView urlText;
    JzvdStd jzvdStd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        urlText = findViewById(R.id.url_text);
        jzvdStd = findViewById(R.id.jz_video);


        String url = urlText.getText().toString().trim();

        jzvdStd.setUp(url, "惊奇队长·逆转无限");
        jzvdStd.thumbImageView.setImageResource(R.mipmap.logo);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
