package com.simplation.androiddemos.function_summary.baidu_track.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.simplation.androiddemos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 日期对话框
 */
public class DateDialog extends Dialog implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {
    private Calendar calendar = null;
    private SimpleDateFormat simpleDateFormat = null;
    private Callback callback = null;
    private DatePicker datePicker = null;
    private TimePicker timePicker = null;
    private String dateTime;

    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;

    /**
     * @param activity ：调用的父activity
     */
    public DateDialog(Activity activity, Callback callback) {
        super(activity, android.R.style.Theme_Holo_Light_Dialog);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        dateTime = simpleDateFormat.format(System.currentTimeMillis());
        this.setTitle(dateTime);
        this.callback = callback;
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        ViewUtil.resizePicker(datePicker);
        ViewUtil.resizePicker(timePicker);
        Button cancelBtn = findViewById(R.id.btn_cancel);
        Button sureBtn = findViewById(R.id.btn_sure);
        cancelBtn.setOnClickListener(view -> DateDialog.this.dismiss());
        sureBtn.setOnClickListener(view -> {
            if (null != callback) {
                long timeStamp = calendar.getTime().getTime() / 1000;
                DateDialog.this.callback.onDateCallback(timeStamp);
            }
            DateDialog.this.dismiss();
        });

        datePicker.init(this.year, this.month, this.day, this);
        timePicker.setOnTimeChangedListener(this);
        timePicker.setIs24HourView(true);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        updateDate();
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        updateDate();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void updateDate() {
        calendar.set(this.year, this.month, this.day, this.hour, this.minute);
        dateTime = simpleDateFormat.format(calendar.getTime());
        this.setTitle(dateTime);
    }

    public interface Callback {
        void onDateCallback(long timeStamp);
    }
}
