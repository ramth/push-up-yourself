package com.example.secomd.pushupsensor;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by secomd on 1/5/2017.
 *
 * Class that handles creation, update, and passes the current database object upon instantiation
 */

public class AlarmEntryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AlarmEntry.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmEntry.AlarmEntryTable.TABLE_NAME + " (" +
                    AlarmEntry.AlarmEntryTable._ID + " INTEGER PRIMARY KEY," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR + " INTEGER," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE + " INTEGER," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT + " INTEGER," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_REPEATING + " INTEGER," +
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_DAY + " TEXT,"+
                    AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmEntry.AlarmEntryTable.TABLE_NAME;

    public AlarmEntryDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);

        //Log.v("tried","creating");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Log.v("tried", "creating");
            db.execSQL(SQL_CREATE_ENTRIES);

        } catch (SQLException e) {
            Log.v("ok", "what");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.v("tried", "upgrading");
    }
}
