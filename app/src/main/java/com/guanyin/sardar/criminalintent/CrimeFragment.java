package com.guanyin.sardar.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.guanyin.sardar.criminalintent.model.Crime;
import com.guanyin.sardar.criminalintent.model.CrimeLab;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IllegalFormatCodePointException;
import java.util.UUID;


// 管理陋习的编辑界面
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    EditText mTitleField;

    Button mDateButton;
    Button mTimeButton;
    Button mReportButton;
    Button mSuspectButton;
    CheckBox mSolvedCheckbox;

    // 从date对象中获取的字段
    int hour;
    int minute;

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    // 使用fragment args的做法
    // 1.声明当前fragment的参数名称
    // 2.创建newInstance的方法用于封装增加args到当前fragment的行为

    private static final String ARG_CRIME_ID = "crime_id";


    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        // 这一句代码告诉托管当前fragment的activity，当前fragment可以处理创建菜单的请求
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);


        // 发送当前crime的报告
        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts
                .CONTENT_URI);
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        // 在edittext控件中对陋习的标题进行修改
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 设置按钮的文本并禁用它的点击功能
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        // 拓展点击功能
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        // 额外的设置小时和分钟的按钮
        mTimeButton = (Button) view.findViewById(R.id.crime_time);

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timeDialog.show(manager, DIALOG_TIME);
            }
        });
        // 更新视图的内容
        updateUI();
        // 设置根据复选框的状态改变crime的属性（solved属性）
        mSolvedCheckbox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crime_delete:
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.delCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            // 更新视图
            mDateButton.setText(mCrime.getDate().toString());
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            // 第一步：从原有的date中拿到年月日的信息
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTime(mCrime.getDate());
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            // 第二步：从传过来的数据中拿到小时、分钟的信息
            Calendar mCalendar1 = Calendar.getInstance();
            mCalendar1.setTime(date);
            int hour = mCalendar1.get(Calendar.HOUR);
            int minute = mCalendar1.get(Calendar.MINUTE);
            // 第三步：合并数据，并保存在crime对象中
            Calendar calendar = new GregorianCalendar(year, month, day, hour, minute);
            mCrime.setDate(calendar.getTime());
            // 更新视图
            updateUI();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields,
                    null, null, null);
            try {
                if ((cursor != null ? cursor.getCount() : 0) == 0) {
                    return;
                }
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                cursor.close();
            }
        }
    }

    private void updateUI() {
        // 根据当前的crime的数据更新视图
        getTime(mCrime.getDate());
        StringBuilder stringBuffer = new StringBuilder();
        if (hour < 10) {
            stringBuffer.append("0").append(hour).append(":");
        } else {
            stringBuffer.append(hour).append(":");
        }
        if (minute < 10) {
            stringBuffer.append("0").append(minute);
        } else {
            stringBuffer.append(minute);
        }
        mTimeButton.setText(stringBuffer.toString());
        mDateButton.setText(mCrime.getDate().toString());
    }

    // 从当前的crime中获取到需要发送的内容
    private String getCrimeReport() {
        String solvedString;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString,
                solvedString, suspect);

    }


    private void getTime(Date date) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        hour = mCalendar.get(Calendar.HOUR);
        minute = mCalendar.get(Calendar.MINUTE);
    }
}
