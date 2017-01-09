package com.example.secomd.pushupsensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by secomd on 1/5/2017.
 */

public class AlarmEntryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AlarmEntry.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmEntry.AlarmEntryTable.TABLE_NAME + " (" +
                    AlarmEntry.AlarmEntryTable._ID + " INTEGER PRIMARY KEY," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_TIME + " TEXT," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_DATE + " TEXT" +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT + "INTEGER" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmEntry.AlarmEntryTable.TABLE_NAME;

    public AlarmEntryDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}
