package com.guanyin.sardar.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.guanyin.sardar.criminalintent.utils.SingleFragmentActivity;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.guanyin.sardar.crimeinalintent.crime_id";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.newInstance(crimeId);
    }

    // 使用intent传递附加信息到当前的activity时
    // 在当前的activity中创建需要传递的键值对的键
    // 新建如下方法完成“创建跳转到当前activity的intent”的封装
    public static Intent newIntent(Context packageContext, UUID uuid) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;
    }

}

