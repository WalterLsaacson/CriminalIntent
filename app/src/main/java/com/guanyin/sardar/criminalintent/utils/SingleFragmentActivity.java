package com.guanyin.sardar.criminalintent.utils;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.guanyin.sardar.criminalintent.CrimeFragment;
import com.guanyin.sardar.criminalintent.CrimeListFragment;
import com.guanyin.sardar.criminalintent.R;

/**
 * Created by Sardar on 2016/12/9.
 */
// 抽象类对所有将持有fragment引用的activity的抽象
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CrimeListFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
