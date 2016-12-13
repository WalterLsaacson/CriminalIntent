package com.guanyin.sardar.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.guanyin.sardar.criminalintent.database.CrimeDbSchema.*;

// 添加数据库的步骤
// 1.生成数据库的schema
// 2.新建类继承自SQLiteOpenHelper，负责更新和升级工作
// 3.在模型层替换原来的arrayList为当前的数据库（增删改查）
// 3.1：增加insert 3.2: 删除del 3.3 修改update
// 3.4 查询query   这里需要将数据库的数据和crime这个类对象进行相互转换
// 具体的为新建一个继承自cursorWrapper的类，封装转换的过程，返回一个crime对象
// 4.对于用户的操作只需要在模型层进行相关的增删改查 然后通知视图层进行刷新就可以了（关键就在于视图层刷新的时机）

// 继承自SQLiteOpenHelper，用于数据库的创建和更新工作
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crime.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 初始化的时候创建一个crimeTable
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                CrimeTable.Cols.UUID + "," +
                CrimeTable.Cols.TITLE + "," +
                CrimeTable.Cols.DATE + "," +
                CrimeTable.Cols.SOLVED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
