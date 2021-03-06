package com.guanyin.sardar.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


// 日期选择的fragment 使用fragment的args方法进行设置展示的日期

// 当前类中包含从date对象获取年月日和从datePicker中获取年月日转换为date对象的转换方法
public class TimePickerFragment extends DialogFragment {

    Calendar mCalendar;

    int hour;
    int minute;

    private static final String ARG_TIME = "time";
    public static final String EXTRA_TIME = "com.guanyin.sardar.criminalintent.time";

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    private void getTime(Date date) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        hour = mCalendar.get(Calendar.HOUR);
        minute = mCalendar.get(Calendar.MINUTE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        // 从参数中获取date对象
        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        getTime(date);


        final TimePicker mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);

        mTimePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            int hour = mTimePicker.getHour();
                            int minute = mTimePicker.getMinute();
                            Calendar calendar = new GregorianCalendar(0, 0, 0, hour, minute);
                            Date date1 = calendar.getTime();
                            sendResult(Activity.RESULT_OK, date1);
                        }
                    }
                })
                .create();
    }

    private void sendResult(int resultOk, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, intent);
    }


}
