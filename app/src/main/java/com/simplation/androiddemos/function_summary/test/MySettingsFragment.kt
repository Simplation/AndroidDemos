package com.simplation.androiddemos.function_summary.test

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.luck.picture.lib.tools.ToastUtils
import com.simplation.androiddemos.R

/**
 * @作者: Simplation
 * @日期: 2021/5/28 15:18
 * @描述:
 * @更新:
 */
class MySettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        findPreference<CheckBoxPreference>("CheckBoxPreference")?.setOnPreferenceClickListener {
            ToastUtils.s(context, "确定清理缓存吗?")
            false
        }

        findPreference<Preference>("todo")?.setOnPreferenceClickListener {
            ToastUtils.s(context, "Todo?")
            false
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        if (key == "CheckBoxPreference") {
            // CacheUtil.setIsNeedTop(sharedPreferences.getBoolean("top", true))
            Log.d("TAG", "onSharedPreferenceChanged: $key - $sharedPreferences")
        }

        if (key == "todo") {
            // 将数据保存，返回页面并重新进入之后依旧显示原来的选项
            // CacheUtil.setIsNeedTop(sharedPreferences.getBoolean("top", true))
            Log.d("TAG", "onSharedPreferenceChanged: $key - $sharedPreferences")
        }
    }


}