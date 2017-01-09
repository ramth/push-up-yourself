package com.example.secomd.pushupsensor;

import android.provider.BaseColumns;

/**
 * Created by secomd on 1/5/2017.
 */

public final class AlarmEntry {

    private AlarmEntry() {};

    public static class AlarmEntryTable implements BaseColumns {
        public static final String TABLE_NAME = "Alarms";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_PUSHUPCOUNT = "Count integer";

    }
}
