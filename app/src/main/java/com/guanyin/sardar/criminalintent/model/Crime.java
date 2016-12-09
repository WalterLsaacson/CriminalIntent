package com.guanyin.sardar.criminalintent.model;

import java.util.Date;
import java.util.UUID;

// 该应用需要记录的单个陋习条目
public class Crime {
    // uuid 唯一标示这个crime条目
    private UUID mId;
    // 这个陋习的标题
    private String mTitle;
    // 这个陋习被添加的时间
    private Date mDate;
    // 这个陋习是否已经被解决
    private boolean mSolved;

    public UUID getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();

    }
}
