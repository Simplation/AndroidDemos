package com.simplation.androiddemos.function_summary.token.http.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.simplation.androiddemos.function_summary.token.http.GlobalToken;
import com.simplation.androiddemos.function_summary.token.http.IGlobalManager;
import com.simplation.androiddemos.function_summary.token.http.RetrofitUtil;
import com.simplation.androiddemos.function_summary.token.http.api.IApiService;
import com.simplation.androiddemos.function_summary.token.http.api.TokenModel;
import com.simplation.androiddemos.function_summary.token.http.exception.TokenInvalidException;
import com.simplation.androiddemos.function_summary.token.http.exception.TokenNotExistException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @作者: Simplation
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
public class ProxyHandler implements InvocationHandler {

    private final static String TAG = "Token_Proxy";

    private final static String TOKEN = "token";

    private final static int REFRESH_TOKEN_VALID_TIME = 30;
    private static long tokenChangedTime = 0;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;

    private Object mProxyObject;
    private IGlobalManager mGlobalManager;

    public ProxyHandler(Object proxyObject, RetrofitUtil globalManager) {
        mProxyObject = proxyObject;
        mGlobalManager = globalManager;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        return Observable.just(null).flatMap((Func1<Object, Observable<?>>) o -> {
            try {
                try {
                    if (mIsTokenNeedRefresh) {
                        updateMethodToken(method, args);
                    }
                    return (Observable<?>) method.invoke(mProxyObject, args);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).retryWhen(observable -> observable.flatMap((Func1<Throwable, Observable<?>>) throwable -> {
            if (throwable instanceof TokenInvalidException) {
                return refreshTokenWhenTokenInvalid();
            } else if (throwable instanceof TokenNotExistException) {
                // Token 不存在，执行退出登录的操作。（为了防止多个请求，都出现 Token 不存在的问题，
                // 这里需要取消当前所有的网络请求）
                mGlobalManager.exitLogin();
                return Observable.error(throwable);
            }
            return Observable.error(throwable);
        }));
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            // Have refreshed the token successfully in the valid time.
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                mIsTokenNeedRefresh = true;
                return Observable.just(true);
            } else {
                // call the refresh token api.
                RetrofitUtil.getInstance().get(IApiService.class).refreshToken().subscribe(new Subscriber<TokenModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshTokenError = e;
                    }

                    @Override
                    public void onNext(TokenModel model) {
                        if (model != null) {
                            mIsTokenNeedRefresh = true;
                            tokenChangedTime = new Date().getTime();
                            GlobalToken.updateToken(model.token);
                            Log.d(TAG, "Refresh token success, time = " + tokenChangedTime);
                        }
                    }
                });
                if (mRefreshTokenError != null) {
                    return Observable.error(mRefreshTokenError);
                } else {
                    return Observable.just(true);
                }
            }
        }
    }

    /**
     * Update the token of the args in the method.
     *
     * PS： 因为这里使用的是 GET 请求，所以这里就需要对 Query 的参数名称为 token 的方法。
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(GlobalToken.getToken())) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Query) {
                            if (TOKEN.equals(((Query) annotation).value())) {
                                args[i] = GlobalToken.getToken();
                            }
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }
}
