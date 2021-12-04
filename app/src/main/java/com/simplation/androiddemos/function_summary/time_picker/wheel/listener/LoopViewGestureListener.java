package com.simplation.androiddemos.function_summary.time_picker.wheel.listener;

import android.view.MotionEvent;

import com.simplation.androiddemos.function_summary.time_picker.wheel.WheelView;

/**
 * 手势监听
 */
public class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final WheelView wheelView;

    public LoopViewGestureListener(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}