package com.simplation.androiddemos.function_summary.token.http.api;

import com.google.gson.annotations.SerializedName;

/**
 * @作者: Simplation
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
public class ApiModel<T> {

    public boolean success;

    @SerializedName("error_code")
    public int errorCode;

    public T data;
}
