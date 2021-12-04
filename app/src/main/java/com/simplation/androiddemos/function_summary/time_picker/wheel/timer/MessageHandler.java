package com.simplation.androiddemos.function_summary.time_picker.wheel.timer;

import android.os.Handler;
import android.os.Message;

import com.simplation.androiddemos.function_summary.time_picker.wheel.WheelView;

/**
 * Handler 消息类
 */
public class MessageHandler extends Handler {

    static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    static final int WHAT_SMOOTH_SCROLL = 2000;
    static final int WHAT_ITEM_SELECTED = 3000;

    private final WheelView wheelView;

    public MessageHandler(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                wheelView.invalidate();
                break;

            case WHAT_SMOOTH_SCROLL:
                wheelView.smoothScroll(WheelView.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                wheelView.onItemSelected();
                break;
        }
    }
}
