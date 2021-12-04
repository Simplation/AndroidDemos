package com.simplation.androiddemos.function_summary.take_photo.code.bean

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_preference.*


class AvoidOnResult(activity: Activity) {
    private val TAG = "AvoidOnResult"
    private var mAvoidOnResultFragment: AvoidOnResultFragment? = null

    init {
        mAvoidOnResultFragment = getAvoidOnResultFragment(activity)
    }

    private fun getAvoidOnResultFragment(activity: Activity): AvoidOnResultFragment {
        var avoidOnResultFragment: AvoidOnResultFragment? = findAvoidOnResultFragment(activity)
        if (avoidOnResultFragment == null) {
            avoidOnResultFragment = AvoidOnResultFragment()
            val fragmentManager: FragmentManager = activity.host_fragment as FragmentManager
            fragmentManager
                .beginTransaction()
                .add(avoidOnResultFragment, TAG)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return avoidOnResultFragment
    }

    private fun findAvoidOnResultFragment(activity: Activity): AvoidOnResultFragment {
        return activity.fragmentManager.findFragmentByTag(TAG) as AvoidOnResultFragment
    }

    //    public Observable<ActivityResultInfo> startForResult(Intent intent) {
    //        return mAvoidOnResultFragment.startForResult(intent);
    //    }
    //
    //    public Observable<ActivityResultInfo> startForResult(Class<?> clazz) {
    //        Intent intent = new Intent(mAvoidOnResultFragment.getActivity(), clazz);
    //        return startForResult(intent);
    //    }
    fun startForResult(intent: Intent?, callback: Callback?) {
        mAvoidOnResultFragment!!.startForResult(intent, callback!!)
    }

    fun startForResult(clazz: Class<*>?, callback: Callback?) {
        val intent = Intent(mAvoidOnResultFragment!!.activity, clazz)
        startForResult(intent, callback)
    }

    interface Callback {
        fun onActivityResult(resultCode: Int, data: Intent?)
    }

}
