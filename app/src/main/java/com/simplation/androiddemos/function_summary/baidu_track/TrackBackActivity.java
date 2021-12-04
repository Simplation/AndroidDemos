package com.simplation.androiddemos.function_summary.baidu_track;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.simplation.androiddemos.R;
import com.simplation.androiddemos.base.BaseActivity;
import com.simplation.androiddemos.base.BaseApplication;
import com.simplation.androiddemos.function_summary.baidu_track.utils.CommonUtil;
import com.simplation.androiddemos.function_summary.baidu_track.utils.Constants;
import com.simplation.androiddemos.function_summary.baidu_track.utils.MapUtil;
import com.simplation.androiddemos.function_summary.baidu_track.utils.ViewUtil;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TrackBackActivity extends BaseActivity {

    private BaseApplication trackApp = null;

    private ViewUtil viewUtil = null;

    private LinearLayout backLayout = null;

    private TextView optionsName = null;

    /**
     * 地图工具
     */
    private MapUtil mapUtil = null;

    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();

    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 查询轨迹的开始时间
     */
    private long startTime = CommonUtil.getCurrentTime();

    /**
     * 查询轨迹的结束时间
     */
    private long endTime = CommonUtil.getCurrentTime();

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();

    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    private int pageIndex = 1;

    private List<LatLng> latLngList = new ArrayList<>();

    private int mCurrentDirection = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_track_back;
    }

    @Override
    protected void setView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        setTitle(R.string.track_back_title);
        trackApp = (BaseApplication) getApplicationContext();
        init();
    }

    /*private void setOnClickListener(View.OnClickListener listener) {
        LinearLayout layout = findViewById(R.id.layout_top);
        LinearLayout optionsButton = layout.findViewById(R.id.btn_activity_options);
        optionsButton.setOnClickListener(listener);
    }*/

    private void init() {
        backLayout = findViewById(R.id.btn_activity_back);
        backLayout.setOnClickListener(v -> finish());

        optionsName = findViewById(R.id.tv_options);
        optionsName.setText("回放条件设置");
        optionsName.setOnClickListener(view -> {
            Intent intent = new Intent(this, TrackQueryOptionsActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);

        });

        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.init(findViewById(R.id.track_back_mapView));
        mapUtil.setCenter(mCurrentDirection);//设置地图中心点
        initListener();
    }

    private void initListener() {
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                try {

                    int total = response.getTotal();
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                        viewUtil.showToast(TrackBackActivity.this, response.getMessage());
                    } else if (0 == total) {
                        viewUtil.showToast(TrackBackActivity.this, getString(R.string.no_track_data));
                    } else {
                        // 拿到里查询到的所有的点
                        List<TrackPoint> points = response.getTrackPoints();

                        //定义Maker坐标点
                        LatLng point = new LatLng(response.getStartPoint().getLocation().latitude,
                                response.getStartPoint().getLocation().getLongitude());
                        //构建Marker图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.mipmap.icon_point);
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions option = new MarkerOptions()
                                .position(point)
                                .icon(bitmap);
                        //在地图上添加Marker，并显示
                        mapUtil.baiduMap.addOverlay(option);


                        for (int i = 0; i < points.size(); i++) {
                            LatLng latlng = MapUtil.convertTrace2Map(points.get(i).getLocation());
                            latLngList.add(latlng);
                        }

                        OverlayOptions mOverlayOptions = new PolylineOptions().width(8)
                                .color(0xAAFF0000).points(latLngList);

                        Overlay mOverlay = mapUtil.baiduMap.addOverlay(mOverlayOptions);

                        /*if (null != points) {
                            for (TrackPoint trackPoint : points) {
                                if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                        trackPoint.getLocation().getLongitude())) {
                                    trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                                }
                            }
                        }*/
                    }

                    //查找下一页数据
                    if (total > Constants.PAGE_SIZE * pageIndex) {
                        historyTrackRequest.setPageIndex(++pageIndex);
                        queryHistoryTrack();
                    } else {
                        OverlayOptions mOverlayOptions = new PolylineOptions().width(8)
                                .color(0xAAFF0000).points(latLngList);

                        Overlay mOverlay = mapUtil.baiduMap.addOverlay(mOverlayOptions);
                        // mapUtil.drawHistoryTrack(trackPoints, true, 0);//画轨迹
                    }

                    queryDistance();// 查询里程
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                viewUtil.showToast(TrackBackActivity.this, "里程：" + response.getDistance());
                super.onDistanceCallback(response);
            }

        };
    }

    /**
     * 轨迹查询设置回调
     */
    @Override
    protected void onActivityResult(int historyTrackRequestCode, int resultCode, Intent data) {
        super.onActivityResult(historyTrackRequestCode, resultCode, data);
        if (null == data) {
            return;
        }

        trackPoints.clear();
        pageIndex = 1;

        if (data.hasExtra("startTime")) {
            startTime = data.getLongExtra("startTime", CommonUtil.getCurrentTime());
        }
        if (data.hasExtra("endTime")) {
            endTime = data.getLongExtra("endTime", CommonUtil.getCurrentTime());
        }

        ProcessOption processOption = new ProcessOption();
        if (data.hasExtra("radius")) {
            processOption.setRadiusThreshold(data.getIntExtra("radius", Constants.DEFAULT_RADIUS_THRESHOLD));
        }
        processOption.setTransportMode(TransportMode.walking);

        if (data.hasExtra("denoise")) {//去噪
            processOption.setNeedDenoise(data.getBooleanExtra("denoise", true));
        }
        if (data.hasExtra("vacuate")) {//抽稀
            processOption.setNeedVacuate(data.getBooleanExtra("vacuate", true));
        }
        if (data.hasExtra("mapmatch")) {//绑路
            processOption.setNeedMapMatch(data.getBooleanExtra("mapmatch", true));
        }
        historyTrackRequest.setProcessOption(processOption);

        if (data.hasExtra("processed")) {//纠偏
            historyTrackRequest.setProcessed(data.getBooleanExtra("processed", true));
        }

        queryHistoryTrack();
    }

    /**
     * 查询里程数
     */
    private void queryDistance() {
        DistanceRequest distanceRequest = new DistanceRequest(trackApp.getTag(), trackApp.getServiceId(), trackApp.getEntityName());
        distanceRequest.setStartTime(startTime);// 设置开始时间
        distanceRequest.setEndTime(endTime);// 设置结束时间
        distanceRequest.setProcessed(true);// 纠偏
        ProcessOption processOption = new ProcessOption();// 创建纠偏选项实例
        processOption.setNeedDenoise(true);// 去噪
        processOption.setNeedMapMatch(true);// 绑路
        processOption.setTransportMode(TransportMode.walking);// 交通方式为步行
        distanceRequest.setProcessOption(processOption);// 设置纠偏选项
        distanceRequest.setSupplementMode(SupplementMode.no_supplement);// 里程填充方式为无
        BaseApplication.mClient.queryDistance(distanceRequest, mTrackListener);// 查询里程
    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        trackApp.initRequest(historyTrackRequest);
        historyTrackRequest.setSupplementMode(SupplementMode.no_supplement);
        historyTrackRequest.setSortType(sortType);
        historyTrackRequest.setCoordTypeOutput(CoordType.bd09ll);
        historyTrackRequest.setEntityName(trackApp.getEntityName());
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        BaseApplication.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    public void setOptionsText() {
        LinearLayout layout = findViewById(R.id.layout_top);
        TextView textView = layout.findViewById(R.id.tv_options);
        textView.setText("回放条件设置");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != trackPoints) {
            trackPoints.clear();
        }

        trackPoints = null;
        mapUtil.clear();

    }

}
