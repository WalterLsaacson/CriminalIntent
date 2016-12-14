package com.guanyin.sardar.criminalintent.database;

// 当前类用于创建该应用的数据库字段名称 即：数据库模型
public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";

        }
    }
}
