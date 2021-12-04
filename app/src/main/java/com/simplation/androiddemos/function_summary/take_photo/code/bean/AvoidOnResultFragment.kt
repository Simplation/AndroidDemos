package com.simplation.androiddemos.function_summary.take_photo.code.bean

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.HashMap

class AvoidOnResultFragment : Fragment() {

    // private Map<Integer, PublishSubject<ActivityResultInfo>> mSubjects = new HashMap<>();
    private val mCallbacks: MutableMap<Int, AvoidOnResult.Callback> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

//    public Observable<ActivityResultInfo> startForResult(final Intent intent) {
//        final PublishSubject<ActivityResultInfo> subject = PublishSubject.create();
//        return subject.doOnSubscribe(new Consumer<Disposable>() {
//            @Override
//            public void accept(Disposable disposable) throws Exception {
//                int requestCode = generateRequestCode();
//                mSubjects.put(requestCode, subject);
//                startActivityForResult(intent, requestCode);
//            }
//        });
//    }

    fun startForResult(intent: Intent?, callback: AvoidOnResult.Callback) {
        val requestCode = generateRequestCode()
        mCallbacks[requestCode] = callback
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // rxjava 方式的处理
//        PublishSubject<ActivityResultInfo> subject = mSubjects.remove(requestCode);
//        if (subject != null) {
//            subject.onNext(new ActivityResultInfo(resultCode, data));
//            subject.onComplete();
//        }

        // callback 方式的处理
        val callback: AvoidOnResult.Callback? = mCallbacks.remove(requestCode)
        callback?.onActivityResult(resultCode, data)
    }

    private fun generateRequestCode(): Int {
        val random = Random()
        while (true) {
            val code: Int = random.nextInt(65536)
            //  if (!mSubjects.containsKey(code) && !mCallbacks.containsKey(code)){
            //      return code;
            //  }
            if (!mCallbacks.containsKey(code)) {
                return code
            }
        }
    }
}