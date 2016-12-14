package com.guanyin.sardar.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.guanyin.sardar.criminalintent.database.CrimeBaseHelper;
import com.guanyin.sardar.criminalintent.database.CrimeCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.guanyin.sardar.criminalintent.database.CrimeDbSchema.CrimeTable;


// 维护一个list来保存已经发现的陋习，生命周期和本应用一样长
public class CrimeLab {


    private static CrimeLab sCrimeLab;

    // 声明数据库字段
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        // 这一句调用CrimeBaseHelper用于创建一个新的数据库或者返回已经存在的数据库
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs,
                null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

    // 获取陋习列表
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            crimes.add(cursorWrapper.getCrime());
            cursorWrapper.moveToNext();
        }

        cursorWrapper.close();

        return crimes;
    }

    // 根据UUID获取唯一的那个陋习
    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?", new
                String[]{id.toString()});
        try {
            if (cursorWrapper.getCount() == 0)
                return null;
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    // 更新数据库
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mSQLiteDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new
                String[]{uuidString});
    }

    // 添加crime到列表
    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);

        mSQLiteDatabase.insert(CrimeTable.NAME, null, values);
    }

    //删除crime到列表
    public void delCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        mSQLiteDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new
                String[]{uuidString});
    }

    // 从crime对象转换到数据库可以识别的contentValues键值对对象
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());

        values.put(CrimeTable.Cols.DATE, crime.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

}
