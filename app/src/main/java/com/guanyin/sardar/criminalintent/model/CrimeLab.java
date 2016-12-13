package com.guanyin.sardar.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


// 维护一个list来保存已经发现的陋习，生命周期和本应用一样长
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        // 初始化100条陋习
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }

    // 获取陋习列表
    public List<Crime> getCrimes() {
        return mCrimes;
    }

    // 根据UUID获取唯一的那个陋习
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes
                ) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    // 添加crime到列表
    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    //添加crime到列表
    public void delCrime(Crime crime) {
        mCrimes.remove(crime);
    }

}
