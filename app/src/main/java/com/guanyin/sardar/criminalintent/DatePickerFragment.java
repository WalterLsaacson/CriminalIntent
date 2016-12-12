package com.guanyin.sardar.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


// 日期选择的fragment 使用fragment的args方法进行设置展示的日期

// 当前类中包含从date对象获取年月日和从datePicker中获取年月日转换为date对象的转换方法
public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    private static final String EXTRA_DATE = "com.guanyin.sardar.criminalintent.date";

    // 使用fragment的设置参数方法的通用做法
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        // 获取当前fragment的参数并设置日期选择控件的初始日期
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePicker mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date1 = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date1);
                    }
                })
                .create();
    }

    // 同一个activity托管的两个fragment之间传递数据
    // 1.设置targetFragment
    // 2.调用父fragment的onActivityResult方法，使用intent进行发送数据
    // 3.在父fragment中重写onActivityResult方法，完成接受子fragment的数据
    // 总结：这里的原理是 父fragment调用子fragment的show方法后，在其被取消之后会调用父fragment的onActivityResult方法
    // 以此类推 父activity启动子activity之后，在子activity被销毁之后会调用父activity的onActivityResult方法
    // 如果是在父activity的fragment中启动的子activity，那么在这之后会回调用特定fragment的onActivityResult方法
    private void sendResult(int requestCode, Date date) {
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), requestCode, intent);
    }

}
