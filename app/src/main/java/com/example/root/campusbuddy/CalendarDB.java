package com.example.root.campusbuddy;

import android.provider.BaseColumns;

/**
 * Created by rc on 13/5/15.
 */


public final class CalendarDB {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public CalendarDB() {}

    /* Inner class that defines the table contents */
    public static abstract class CalendarEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_STARTTIME = "time1";
        public static final String COLUMN_NAME_ENDTIME = "time2";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_NAME_VENUE = "venue";

    }
}
