package com.simplation.androiddemos.function_summary.take_photo.code.callback


interface PermissionCallback {
    /**
     * 权限获取成功
     */
    fun onPermissionSuccess()

    /**
     * 权限获取失败
     */
    fun onPermissionFailure(toTypedArray: Array<String?>)
}