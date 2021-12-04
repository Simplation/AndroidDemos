package com.simplation.androiddemos.function_summary.token.http;

/**
 * @作者: W ◕‿-｡ Z
 * @日期: 2020/3/17
 * @描述:
 * @更新:
 */
public class GlobalToken {

    private static String sToken;

    public static synchronized void updateToken(String token) {
        sToken = token;
    }

    public static String getToken() {
        return sToken;
    }
}
