package com.simplation.androiddemos.function_summary.imitate_fish;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

public class ImitateFishActivity extends BaseActivity {

    private PublishDialog publishDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imitate_fish;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        Button openDialog = findViewById(R.id.open_dialog);
        openDialog.setOnClickListener(v -> {
            if (publishDialog == null) {
                publishDialog = new PublishDialog(ImitateFishActivity.this);
                publishDialog.setFabuClickListener(v1 ->
                        Toast.makeText(ImitateFishActivity.this, "发布", Toast.LENGTH_SHORT).show()
                );

                publishDialog.setHuishouClickListener(v12 ->
                        Toast.makeText(ImitateFishActivity.this, "回收", Toast.LENGTH_SHORT).show()
                );

                publishDialog.setPingguClickListener(v13 ->
                        Toast.makeText(ImitateFishActivity.this, "评估", Toast.LENGTH_SHORT).show())
                ;
            }
            publishDialog.show();
        });
    }
}
