package com.simplation.androiddemos.function_summary.baidu_track;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.trace.model.LocationMode;
import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;
import com.simplation.androiddemos.function_summary.baidu_track.utils.Constants;

import org.jetbrains.annotations.Nullable;

import static com.baidu.trace.model.LocationMode.High_Accuracy;

public class TracingOptionsActivity extends BaseActivity {

    // 返回结果
    private Intent result = null;

    private EditText gatherIntervalText = null;
    private EditText packIntervalText = null;

    private LinearLayout backLayout = null;

    private LinearLayout optionsLayout = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tracing_options;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(R.string.tracing_options_title);
        init();
    }

    private void init() {
        backLayout = findViewById(R.id.btn_activity_back);
        optionsLayout = findViewById(R.id.btn_activity_options);

        optionsLayout.setVisibility(View.INVISIBLE);

        backLayout.setOnClickListener(view -> {
            finish();
        });

        gatherIntervalText = findViewById(R.id.gather_interval);
        packIntervalText = findViewById(R.id.pack_interval);

        gatherIntervalText.setOnFocusChangeListener((view, hasFocus) -> {
            EditText textView = (EditText) view;
            String hintStr = textView.getHint().toString();
            if (hasFocus) {
                textView.setHint("");
            } else {
                textView.setHint(hintStr);
            }
        });

        packIntervalText.setOnFocusChangeListener((view, hasFocus) -> {
            EditText textView = (EditText) view;
            String hintStr = textView.getHint().toString();
            if (hasFocus) {
                textView.setHint("");
            } else {
                textView.setHint(hintStr);
            }
        });

    }

    public void onCancel(View v) {
        super.onBackPressed();
    }

    public void onFinish(View v) {
        result = new Intent();

        RadioGroup locationModeGroup = findViewById(R.id.location_mode);
        RadioButton locationModeRadio = findViewById(locationModeGroup.getCheckedRadioButtonId());
        LocationMode locationMode = High_Accuracy;//定位模式
        switch (locationModeRadio.getId()) {
            case R.id.device_sensors:
                locationMode = LocationMode.Device_Sensors;
                break;

            case R.id.battery_saving:
                locationMode = LocationMode.Battery_Saving;
                break;

            case R.id.high_accuracy:
                locationMode = High_Accuracy;
                break;

            default:
                break;
        }
        result.putExtra("locationMode", locationMode.name());

        EditText gatherIntervalText = findViewById(R.id.gather_interval);
        EditText packIntervalText = findViewById(R.id.pack_interval);
        String gatherIntervalStr = gatherIntervalText.getText().toString();
        String packIntervalStr = packIntervalText.getText().toString();

        if (!TextUtils.isEmpty(gatherIntervalStr)) {//采集频率
            try {
                result.putExtra("gatherInterval", Integer.parseInt(gatherIntervalStr));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(packIntervalStr)) {//打包频率
            try {
                result.putExtra("packInterval", Integer.parseInt(packIntervalStr));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        setResult(Constants.RESULT_CODE, result);
        super.onBackPressed();
    }

}
