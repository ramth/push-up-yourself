package com.example.secomd.pushupsensor;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.CursorAdapter;

import static com.example.secomd.pushupsensor.AlarmManagerHelper.removeAlarmRepeating;

/**
 * Created by secomd on 3/9/2017.
 *
 * Class that copies the relevant database entry to allow for UI to easily update the alarm and simultaneously update both
 * database and AlarmManager
 */

//TODO how to handle if database entry is destroyed

public class Alarm {

    private boolean DaysEnabled[] = new boolean[] {false, false, false, false, false, false, false};
    private boolean Repeating = false;
    private int hourOfDay = 0;
    private int minute = 0;
    private int UUID;
    private int position;
    private CursorAdapter cursorAdapter;
    public static void updateDatabase() {
        //TODO implement updateDatabase
    }

    public static void updateAlarmManager(){
        // TODO implement updateAlarmManager
    }
    public ContentValues generateContentValues(){
        ContentValues values = new ContentValues();
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT, 30);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR, hourOfDay);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE, minute);
        int repeating  = 0;
        if(isRepeating())  {
            repeating = 1;
        }
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_REPEATING,repeating);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID,UUID);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_DAY,getDaysEnabledString());
        //TODO Implement rest of generateContentValues
        return values;

    }
    public String getDaysEnabledString() {

        String dayEnabledString = "";
        for (int i = 0; i < 7; i++) {
            if (DaysEnabled[i]) {

                dayEnabledString.concat("1");
            }
            else {

                dayEnabledString.concat("0");
            }
        }
        return dayEnabledString;

    }
    public void updateAlarmState(SQLiteDatabase dbWrite, AlarmManager alarmManager, Context context) {
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms WHERE UUID=?", new String[]{Integer.toString(UUID)});
        Log.v("dump", DatabaseUtils.dumpCursorToString(cursor));
        dbWrite.update(AlarmEntry.AlarmEntryTable.TABLE_NAME, generateContentValues(), "UUID=" + UUID, null);
        Cursor cursor_post = dbWrite.rawQuery("SELECT * FROM Alarms WHERE UUID=?", new String[]{Integer.toString(UUID)});
        Log.v("dump", DatabaseUtils.dumpCursorToString(cursor_post));
        //cursor.moveToFirst();
        //int UUID = cursor.getInt(cursor.getColumnIndex("UUID"));
        //String UUID_str = "empty";
        Log.v("grabbed UUID", "");

        //Remove all currently set alarms by creating new Pendingintents
        removeFromAlarmManager(dbWrite,alarmManager,context);
        removeAlarmRepeating(context,alarmManager,hourOfDay,minute,UUID);
        if (!isRepeating()) {
            AlarmManagerHelper.updateAlarm(context, alarmManager, hourOfDay, minute, UUID);
        }
        else {
            for (int i = 0; i < 7; i ++) {
                if(DaysEnabled[i])
                {
                    AlarmManagerHelper.setRepeatingAlarm(context,alarmManager,hourOfDay,minute,i,UUID);
                }
            }
        }

        Log.v("updated", "entry");
    }
    public void removeCompleteAlarmState(SQLiteDatabase dbWrite, AlarmManager alarmManager, Context context) {
        Cursor cursor = dbWrite.rawQuery("SELECT UUID FROM Alarms WHERE UUID=?", new String[]{Integer.toString(UUID)});
        cursor.moveToFirst();
        int UUID = cursor.getInt(cursor.getColumnIndex("UUID"));
        Log.v("UUID", "");
        dbWrite.delete(AlarmEntry.AlarmEntryTable.TABLE_NAME, "UUID=" + UUID, null);
        AlarmManagerHelper.removeAlarm(context, alarmManager, 0, 0, UUID);
        removeAlarmRepeating(context,alarmManager,hourOfDay,minute,UUID);
        Cursor cursor_swap = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        cursorAdapter.swapCursor(cursor_swap);
        Log.v("deleted", "entry");

    }
    public void removeFromAlarmManager(SQLiteDatabase dbWrite, AlarmManager alarmManager, Context context) {

        Log.v("UUID", "");
        AlarmManagerHelper.removeAlarm(context, alarmManager, 0, 0, UUID);
        removeAlarmRepeating(context,alarmManager,hourOfDay,minute,UUID);
        Cursor cursor_swap = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        cursorAdapter.swapCursor(cursor_swap);
        Log.v("deleted", "entry");
    }

    public Alarm(boolean[] DaysEnabled, boolean Repeating, int hourOfDay, int minute, int UUID) {
        this.DaysEnabled = DaysEnabled;
        this.Repeating = Repeating;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.UUID = UUID;
    }

    private void parseDaysEnabledString(String daysString){
        int i = 0;
        for (char c : daysString.toCharArray()) {
            if(Integer.parseInt(Character.toString(c)) == 1) {
                DaysEnabled[i] = true;
            }
            i++;
        }
    }
    public Alarm(Cursor cursor, CursorAdapter cursorAdapter) {
        parseDaysEnabledString(cursor.getString(cursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_DAY)));
        this.Repeating = (Integer.parseInt(cursor.getString(cursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_REPEATING)))== 1);
        this.hourOfDay = cursor.getInt(cursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR));
        this.minute = cursor.getInt(cursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE));
        this.UUID = cursor.getInt(cursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID));
        position = cursor.getPosition();
        this.cursorAdapter = cursorAdapter;

    }
   public void update() {
       updateDatabase();
       updateAlarmManager();
   }
    //Getter and Setter Methods
   public void updateDay (int day, boolean value) {
       DaysEnabled[day] = value;
   }

   public boolean queryDay (int day) {
       return DaysEnabled[day];
   }


    public boolean isRepeating() {
        return Repeating;
    }

    public void setRepeating(boolean repeating) {
        Repeating = repeating;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }


    public static class AlarmBuilder{

    }
}
