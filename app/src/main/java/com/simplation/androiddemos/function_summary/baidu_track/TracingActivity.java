package com.simplation.androiddemos.function_summary.baidu_track;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;
import com.simplation.androiddemos.base.BaseApplication;
import com.simplation.androiddemos.function_summary.baidu_track.model.CurrentLocation;
import com.simplation.androiddemos.function_summary.baidu_track.utils.CommonUtil;
import com.simplation.androiddemos.function_summary.baidu_track.utils.Constants;
import com.simplation.androiddemos.function_summary.baidu_track.utils.MapUtil;
import com.simplation.androiddemos.function_summary.baidu_track.utils.TrackReceiver;
import com.simplation.androiddemos.function_summary.baidu_track.utils.ViewUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TracingActivity extends BaseActivity implements View.OnClickListener, SensorEventListener {

    private LinearLayout backLayout = null;

    private TextView optionsText = null;

    private BaseApplication trackApp = null;

    private ViewUtil viewUtil = null;

    private Button traceBtn = null;

    private Button gatherBtn = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    private SensorManager mSensorManager;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;

    /**
     * ????????????
     */
    private MapUtil mapUtil = null;

    /**
     * ?????????????????????
     */
    private OnTraceListener traceListener = null;

    /**
     * ???????????????(???????????????????????????????????????)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity?????????(??????????????????????????????)
     */
    private OnEntityListener entityListener = null;

    /**
     * ??????????????????
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    /**
     * ????????????
     */
    public int packInterval = Constants.DEFAULT_PACK_INTERVAL;

    /**
     * ???????????????
     */
    private List<LatLng> trackPoints;

    private boolean firstLocate = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tracing;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitle(R.string.tracing_title);
        // setOptionsText();
        // setOnClickListener(this);
        init();
    }

    private void init() {
        initListener();
        trackApp = (BaseApplication) getApplicationContext();

        backLayout = findViewById(R.id.btn_activity_back);
        optionsText = findViewById(R.id.tv_options);

        optionsText.setText("??????????????????");

        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.init(findViewById(R.id.tracing_mapView));
        mapUtil.setCenter(mCurrentDirection);//?????????????????????
        powerManager = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// ???????????????????????????

        traceBtn = findViewById(R.id.btn_trace);
        gatherBtn = findViewById(R.id.btn_gather);

        backLayout.setOnClickListener(v -> finish());

        // ??????????????????
        optionsText.setOnClickListener(view -> {
            Intent intent = new Intent(this, TracingOptionsActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        });

        traceBtn.setOnClickListener(this);
        gatherBtn.setOnClickListener(this);
        setTraceBtnStyle();
        setGatherBtnStyle();

        trackPoints = new ArrayList<>();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //?????????????????????????????????????????????????????????????????????????????????
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {// ??????????????????1?????????????????????????????????????????????????????????
            mCurrentDirection = (int) x;
            if (!CommonUtil.isZeroPoint(CurrentLocation.latitude, CurrentLocation.longitude)) {
                mapUtil.updateMapLocation(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude), (float) mCurrentDirection);
            }
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_trace:
                if (trackApp.isTraceStarted()) {
                    BaseApplication.mClient.stopTrace(BaseApplication.mTrace, traceListener);//????????????
                } else {
                    BaseApplication.mClient.startTrace(BaseApplication.mTrace, traceListener);//????????????
                }
                break;

            case R.id.btn_gather:
                if (trackApp.isGatherStarted()) {
                    BaseApplication.mClient.stopGather(traceListener);
                } else {
                    BaseApplication.mClient.setInterval(Constants.DEFAULT_GATHER_INTERVAL, packInterval);
                    BaseApplication.mClient.startGather(traceListener);//????????????
                }
                break;

            default:
                break;
        }

    }

    /**
     * ????????????????????????
     */
    private void setTraceBtnStyle() {
        boolean isTraceStarted = trackApp.trackConf.getBoolean("is_trace_started", false);
        if (isTraceStarted) {
            traceBtn.setText(R.string.stop_trace);
            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                    .White, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            } else {
                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            }
        } else {
            traceBtn.setText(R.string.start_trace);
            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.Grey400, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            } else {
                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            }
        }
    }

    /**
     * ????????????????????????
     */
    private void setGatherBtnStyle() {
        boolean isGatherStarted = trackApp.trackConf.getBoolean("is_gather_started", false);
        if (isGatherStarted) {
            gatherBtn.setText(R.string.stop_gather);
            gatherBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.White, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gatherBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            } else {
                gatherBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            }
        } else {
            gatherBtn.setText(R.string.start_gather);
            gatherBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.Grey400, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gatherBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            } else {
                gatherBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            }
        }
    }

    /**
     * ??????????????????
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            trackApp.getCurrentLocation(entityListener, trackListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        trackApp.mClient.stopRealTimeLoc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }

        if (data.hasExtra("locationMode")) {
            LocationMode locationMode = LocationMode.valueOf(data.getStringExtra("locationMode"));
            BaseApplication.mClient.setLocationMode(locationMode);//????????????
        }
        BaseApplication.mTrace.setNeedObjectStorage(false);

        if (data.hasExtra("gatherInterval") && data.hasExtra("packInterval")) {
            int gatherInterval = data.getIntExtra("gatherInterval", Constants.DEFAULT_GATHER_INTERVAL);
            int packInterval = data.getIntExtra("packInterval", Constants.DEFAULT_PACK_INTERVAL);
            TracingActivity.this.packInterval = packInterval;
            BaseApplication.mClient.setInterval(gatherInterval, packInterval);//????????????
        }

    }

    private void initListener() {

        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                //????????????????????????????????????????????????????????????
                try {
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                        return;
                    }

                    LatestPoint point = response.getLatestPoint();
                    if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                            .getLongitude())) {
                        return;
                    }

                    LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
                    if (null == currentLatLng) {
                        return;
                    }

                    if (firstLocate) {
                        firstLocate = false;
                        Toast.makeText(TracingActivity.this, "???????????????????????????...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //???????????????
                    CurrentLocation.locTime = point.getLocTime();
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (trackPoints == null) {
                        return;
                    }
                    trackPoints.add(currentLatLng);

                    mapUtil.drawHistoryTrack(trackPoints, false, mCurrentDirection);//?????????????????????????????????
                } catch (Exception x) {
                    x.printStackTrace();
                }


            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                //??????LBSTraceClient????????????????????????
                try {
                    if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                            location.getLongitude())) {
                        return;
                    }
                    LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                    if (null == currentLatLng) {
                        return;
                    }
                    CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                    CurrentLocation.latitude = currentLatLng.latitude;
                    CurrentLocation.longitude = currentLatLng.longitude;

                    if (null != mapUtil) {
                        mapUtil.updateMapLocation(currentLatLng, mCurrentDirection);//??????????????????
                        mapUtil.animateMapStatus(currentLatLng);//??????
                    }

                } catch (Exception x) {
                    x.printStackTrace();
                }

            }

        };

        traceListener = new OnTraceListener() {

            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                viewUtil.showToast(TracingActivity.this,
                        String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    trackApp.setTraceStarted(true);
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                    setTraceBtnStyle();
                    registerReceiver();
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    trackApp.setTraceStarted(false);
                    trackApp.setGatherStarted(false);
                    // ??????????????????????????????is_trace_started??????????????????????????????????????????????????????????????????????????????
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                    setTraceBtnStyle();
                    setGatherBtnStyle();
                    unregisterPowerReceiver();
                    firstLocate = true;
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    trackApp.setGatherStarted(true);
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
                    setGatherBtnStyle();

                    stopRealTimeLoc();
                    startRealTimeLoc(packInterval);
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.setGatherStarted(false);
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
                    setGatherBtnStyle();

                    firstLocate = true;
                    stopRealTimeLoc();
                    startRealTimeLoc(Constants.LOC_INTERVAL);

                    if (trackPoints.size() >= 1) {
                        try {
                            mapUtil.drawEndPoint(trackPoints.get(trackPoints.size() - 1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };

    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * ???????????????????????????GPS?????????
     */
    @SuppressLint("InvalidWakeLockTag")
    private void registerReceiver() {

        if (trackApp.isRegisterReceiver()) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        trackApp.registerReceiver(trackReceiver, filter);
        trackApp.setRegisterReceiver(true);

    }

    private void unregisterPowerReceiver() {
        if (!trackApp.isRegisterReceiver()) {
            return;
        }
        if (null != trackReceiver) {
            trackApp.unregisterReceiver(trackReceiver);
        }
        trackApp.setRegisterReceiver(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BaseApplication.trackConf.contains("is_trace_started")
                && BaseApplication.trackConf.contains("is_gather_started")
                && BaseApplication.trackConf.getBoolean("is_trace_started", false)
                && BaseApplication.trackConf.getBoolean("is_gather_started", false)) {
            startRealTimeLoc(packInterval);
        } else {
            startRealTimeLoc(Constants.LOC_INTERVAL);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();

        mSensorManager.registerListener((SensorEventListener) this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

        // ???Android 6.0??????????????????????????????????????????doze?????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = trackApp.getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRealTimeLoc();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRealTimeLoc();
        trackPoints.clear();
        trackPoints = null;
        mapUtil.clear();
    }

}
