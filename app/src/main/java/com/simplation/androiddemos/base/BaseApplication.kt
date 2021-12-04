package com.simplation.androiddemos.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.baidu.mapapi.SDKInitializer
import com.baidu.trace.LBSTraceClient
import com.baidu.trace.Trace
import com.baidu.trace.api.entity.LocRequest
import com.baidu.trace.api.entity.OnEntityListener
import com.baidu.trace.api.track.LatestPointRequest
import com.baidu.trace.api.track.OnTrackListener
import com.baidu.trace.model.BaseRequest
import com.baidu.trace.model.OnCustomAttributeListener
import com.baidu.trace.model.ProcessOption
import com.baidu.trace.model.TransportMode
import com.iflytek.cloud.SpeechUtility
import com.simplation.androiddemos.R
import com.simplation.androiddemos.function_summary.baidu_track.utils.CommonUtil
import com.simplation.androiddemos.function_summary.baidu_track.utils.NetUtil
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/18
 * @描述:
 * @更新:
 */
class BaseApplication : Application() {

    /**
     * 服务是否开启标识
     */
    var isTraceStarted = false

    /**
     * 采集是否开启标识
     */
    var isGatherStarted = false

    var isRegisterReceiver = false

    /**
     * 轨迹服务ID
     */
    var serviceId: Long = 213818 // 这里是申请的鹰眼服务id

    /**
     * Entity标识
     */
    var entityName = "myTrace"

    private val mSequenceGenerator =
        AtomicInteger()

    companion object {

        const val TAG = "Android_Demos"

        lateinit var instance: BaseApplication

        var context: Context by Delegates.notNull()
            private set

        lateinit var locRequest: LocRequest

        // var mContext: Context

        lateinit var trackConf: SharedPreferences

        /**
         * 轨迹客户端
         */
        lateinit var mClient: LBSTraceClient

        /**
         * 轨迹服务
         */
        lateinit var mTrace: Trace

        var screenWidth = 0

        var screenHeight = 0
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // 初始化科大讯飞语音识别
        SpeechUtility.createUtility(applicationContext, "appid=" + getString(R.string.app_id))

        entityName = CommonUtil.getImei(this)

        // 若为创建独立进程，则不初始化成员变量
        if ("com.baidu.track:remote" == CommonUtil.getCurProcessName(instance)) {
            return
        }

        SDKInitializer.initialize(instance)
        getScreenSize()
        mClient = LBSTraceClient(instance)
        mTrace = Trace(serviceId, entityName)

        trackConf = getSharedPreferences("track_conf", Context.MODE_PRIVATE)
        locRequest = LocRequest(serviceId)

        mClient.setOnCustomAttributeListener(object : OnCustomAttributeListener {
            override fun onTrackAttributeCallback(): MutableMap<String, String> {
                val map: MutableMap<String, String> =
                    HashMap()
                map["key1"] = "value1"
                map["key2"] = "value2"
                return map
            }

            override fun onTrackAttributeCallback(p0: Long): MutableMap<String, String> {
                val map: MutableMap<String, String> =
                    HashMap()
                map["key1"] = "value1"
                map["key2"] = "value2"
                return map
            }
        })

        clearTraceStatus()
    }

    /**
     * 获取当前位置
     */
    fun getCurrentLocation(
        entityListener: OnEntityListener?,
        trackListener: OnTrackListener?
    ) { // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetworkAvailable(instance)
            && trackConf.contains("is_trace_started")
            && trackConf.contains("is_gather_started")
            && trackConf.getBoolean("is_trace_started", false)
            && trackConf.getBoolean("is_gather_started", false)
        ) {
            val request = LatestPointRequest(getTag(), serviceId, entityName)
            val processOption = ProcessOption()
            processOption.radiusThreshold = 50
            processOption.transportMode = TransportMode.walking
            processOption.isNeedDenoise = true
            processOption.isNeedMapMatch = true
            request.processOption = processOption
            mClient.queryLatestPoint(request, trackListener)
        } else {
            mClient.queryRealTimeLoc(locRequest, entityListener)
        }
    }

    /**
     * 获取屏幕尺寸
     */
    private fun getScreenSize() {
        val dm = resources.displayMetrics
        screenHeight = dm.heightPixels
        screenWidth = dm.widthPixels
    }

    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     *
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    private fun clearTraceStatus() {
        if (trackConf.contains("is_trace_started") || trackConf.contains("is_gather_started")) {
            val editor = trackConf.edit()
            editor.remove("is_trace_started")
            editor.remove("is_gather_started")
            editor.apply()
        }
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    fun initRequest(request: BaseRequest) {
        request.setTag(getTag())
        request.setServiceId(serviceId)
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    fun getTag(): Int {
        return mSequenceGenerator.incrementAndGet()
    }

}