package com.simplation.androiddemos.function_summary.token.http.converter;

import androidx.annotation.Nullable;

import com.google.gson.TypeAdapter;
import com.simplation.androiddemos.function_summary.token.http.api.ApiModel;
import com.simplation.androiddemos.function_summary.token.http.api.ErrorCode;
import com.simplation.androiddemos.function_summary.token.http.exception.ApiException;
import com.simplation.androiddemos.function_summary.token.http.exception.TokenInvalidException;
import com.simplation.androiddemos.function_summary.token.http.exception.TokenNotExistException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @作者: Simplation
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            ApiModel apiModel = (ApiModel) adapter.fromJson(value.charStream());
            if (apiModel.errorCode == ErrorCode.TOKEN_NOT_EXIST) {
                throw new TokenNotExistException();
            } else if (apiModel.errorCode == ErrorCode.TOKEN_INVALID) {
                throw new TokenInvalidException();
            } else if (!apiModel.success) {
                // 特定 API 的错误，在相应的 Subscriber 的 onError 的方法中进行处理
                throw new ApiException();
            } else if (apiModel.success) {
                return apiModel.data;
            }
        } finally {
            value.close();
        }
        return null;
    }
}
