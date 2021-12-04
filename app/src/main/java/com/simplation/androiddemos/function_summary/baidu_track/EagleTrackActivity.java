package com.simplation.androiddemos.function_summary.baidu_track;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;
import com.simplation.androiddemos.base.BaseApplication;
import com.simplation.androiddemos.function_summary.baidu_track.utils.BitmapUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EagleTrackActivity extends BaseActivity {

    private BaseApplication trackApp;

    private SDKReceiver mReceiver;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            assert s != null;
            switch (s) {
                case SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR:
                    Toast.makeText(EagleTrackActivity.this, "apikey验证失败，地图功能无法正常使用", Toast.LENGTH_SHORT).show();
                    break;
                case SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK:
                    Toast.makeText(EagleTrackActivity.this, "apikey验证成功", Toast.LENGTH_SHORT).show();
                    break;
                case SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR:
                    Toast.makeText(EagleTrackActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_eagle_track;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        // apikey的授权需要一定的时间，在授权成功之前地图相关操作会出现异常；apikey授权成功后会发送广播通知，我们这里注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        init();
        BitmapUtil.init();
    }

    private void init() {
        trackApp = (BaseApplication) getApplicationContext();

        Button trace = findViewById(R.id.btn_trace);
        Button query = findViewById(R.id.btn_query);
        Button back = findViewById(R.id.btn_back);

        // 轨迹追踪
        trace.setOnClickListener(v -> {
            Intent intent = new Intent(EagleTrackActivity.this, TracingActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            }
            startActivity(intent);
        });

        // 轨迹查询
        query.setOnClickListener(v -> {
            Intent intent = new Intent(EagleTrackActivity.this, TrackQueryActivity.class);
            startActivity(intent);
        });

        // 历史轨迹回放
        back.setOnClickListener(v -> {
            Intent intent = new Intent(EagleTrackActivity.this, TrackBackActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);

        if (BaseApplication.trackConf.contains("is_trace_started")
                && BaseApplication.trackConf.getBoolean("is_trace_started", true)) {
            // 退出app停止轨迹服务时，不再接收回调，将OnTraceListener置空
            BaseApplication.mClient.setOnTraceListener(null);
            BaseApplication.mClient.stopTrace(BaseApplication.mTrace, null);
            BaseApplication.mClient.clear();
        } else {
            BaseApplication.mClient.clear();
        }

        trackApp.setTraceStarted(false);
        trackApp.setGatherStarted(false);

        SharedPreferences.Editor editor = BaseApplication.trackConf.edit();
        editor.remove("is_trace_started");
        editor.remove("is_gather_started");
        editor.apply();

        BitmapUtil.clear();
    }
}
