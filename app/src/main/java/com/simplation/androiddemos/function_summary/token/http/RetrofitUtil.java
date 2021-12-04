package com.simplation.androiddemos.function_summary.token.http;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.simplation.androiddemos.ui.activity.MainActivity;
import com.simplation.androiddemos.base.BaseApplication;
import com.simplation.androiddemos.function_summary.token.http.converter.GsonConverterFactory;
import com.simplation.androiddemos.function_summary.token.http.proxy.ProxyHandler;

import java.lang.reflect.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
public class RetrofitUtil implements IGlobalManager {

    public static final String API = "http://192.168.14.238:8888/";

    private static Retrofit sRetrofit;
    private static OkHttpClient sOkHttpClient;
    private static RetrofitUtil instance;

    private final static Object mRetrofitLock = new Object();

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            synchronized (mRetrofitLock) {
                if (sRetrofit == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();

                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    clientBuilder.addInterceptor(httpLoggingInterceptor);
                    sOkHttpClient = clientBuilder.build();
                    sRetrofit = new Retrofit.Builder().client(sOkHttpClient)
                            .baseUrl(API)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return sRetrofit;
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    public <T> T get(Class<T> tClass) {
        return getRetrofit().create(tClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> tClass) {
        T t = getRetrofit().create(tClass);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[] { tClass }, new ProxyHandler(t, this));
    }

    @Override
    public void exitLogin() {
        // Cancel all the netWorkRequest
        sOkHttpClient.dispatcher().cancelAll();

        // Goto the home page
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BaseApplication.instance, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BaseApplication.instance.startActivity(intent);
                Toast.makeText(BaseApplication.instance, "Token is not existed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
