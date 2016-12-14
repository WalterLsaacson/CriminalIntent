package com.guanyin.sardar.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.guanyin.sardar.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

import static com.guanyin.sardar.criminalintent.database.CrimeDbSchema.*;

// 这个类存在的意义是封装转换的方法
public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        // 从数据库对象到crime对象的转换
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        // 创建crime对象
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setTitle(title);
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
