package com.simplation.androiddemos.function_summary.ifkey_sdk.voice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.cloud.VoiceWakeuper;
import com.simplation.androiddemos.R;

/**
 * 语音唤醒
 */
public class IvwActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivw);

        (findViewById(R.id.btn_wake)).setOnClickListener(IvwActivity.this);
        (findViewById(R.id.btn_oneshot)).setOnClickListener(IvwActivity.this);
    }


    @Override
    public void onClick(View v) {
        if (null == VoiceWakeuper.createWakeuper(this, null)) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Toast.makeText(this
                    , "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化"
                    , Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = null;
        switch (v.getId()) {
            // 唤醒
            case R.id.btn_wake:
                intent = new Intent(IvwActivity.this, WakeActivity.class);
                startActivity(intent);
                break;

            // 唤醒 + 识别
            case R.id.btn_oneshot:
                intent = new Intent(IvwActivity.this, OneShotActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
