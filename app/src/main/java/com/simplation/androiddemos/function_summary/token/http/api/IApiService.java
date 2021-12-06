package com.simplation.androiddemos.function_summary.token.http.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @作者: Simplation
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
public interface IApiService {
    @GET("get_token")
    Observable<TokenModel> getToken();

    @GET("refresh_token")
    Observable<TokenModel> refreshToken();

    @GET("request")
    Observable<ResultModel> getResult(@Query("token") String token);
}
