package com.example.secomd.pushupsensor;

import android.provider.BaseColumns;

/**
 * SQL Database columns and name definitions
 */

public final class AlarmEntry {

    private AlarmEntry() {};

    public static class AlarmEntryTable implements BaseColumns {
        public static final String TABLE_NAME = "Alarms";

        public static final String COLUMN_NAME_HOUR = "Hour";
        public static final String COLUMN_NAME_MINUTE = "Minute";
        public static final String COLUMN_NAME_UUID = "UUID";
        public static final String COLUMN_NAME_PUSHUPCOUNT = "Count";
        public static final String COLUMN_NAME_REPEATING = "Repeating";
        public static final String COLUMN_NAME_DAY = "Day";
    }
}
