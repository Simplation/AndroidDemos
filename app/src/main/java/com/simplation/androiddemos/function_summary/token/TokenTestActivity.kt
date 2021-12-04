package com.simplation.androiddemos.function_summary.token

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.token.http.GlobalToken
import com.simplation.androiddemos.function_summary.token.http.RetrofitUtil
import com.simplation.androiddemos.function_summary.token.http.api.IApiService
import com.simplation.androiddemos.function_summary.token.http.api.ResultModel
import com.simplation.androiddemos.function_summary.token.http.api.TokenModel
import kotlinx.android.synthetic.main.activity_token_test.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TokenTestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_token_test
    }

    override fun setView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun initView() {
        // 获取 Token
        btn_token_get.setOnClickListener {
            RetrofitUtil.getInstance()
                .get(IApiService::class.java)
                .token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<TokenModel?>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(model: TokenModel?) {
                        if (model != null && !TextUtils.isEmpty(model.token)) {
                            GlobalToken.updateToken(model.token)
                        }
                    }
                })
        }

        // 发送请求
        btn_request.setOnClickListener {
            // 模拟进行五次请求。
            for (i in 0..4) {
                RetrofitUtil.getInstance()
                    .getProxy(IApiService::class.java)
                    .getResult(GlobalToken.getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<ResultModel?>() {
                        override fun onCompleted() {}
                        override fun onError(e: Throwable) {}
                        override fun onNext(model: ResultModel?) {
                            Toast.makeText(
                                this@TokenTestActivity,
                                "结果：" + model!!.result,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}
