package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.UUID;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;



/*
The activity created when the app is started. Displays current alarm list in database.

TODO Include selection of days
TODO Delete alarms that exist in database but not in AlarmManager

 */

/*

Logical Flow overview and other relevant information

Day String Info
Example :
SMWTRFS
0101110 (Stored Day String)

Note static day constants start on Sunday with 0


Repeating alaram is only set if repeating button is active

1. Repeating is active :

    i.Setting time without days selected

        - Set time to current day or tomorrow and highlight the day in the day selector TODO 1,i
        - Set repeating alarms correspondingly
    ii. Setting time with some days selected

        - Set repeating alarms for each day selected TODO 1,ii

2. Repeating is not active :

    i. Setting time without days selected
        - same as 1.i
        - Set non-repeating alarm

    ii. Setting time with days selected
        - Set non-repeating alarm correspondingly TODO 2, ii

3. Repeating changed TODO 3

    i. Change switch to off position
    ii. Clear date information
    iii. Delete all currently set alarms

4. Switch is set to on

    i. send to 1 or 2 TODO 4, i

5. Switch is set to off

    i. delete 1 or 2 TODO 5, i

6. Delete button is pressed

    i. send to 5, i TODO 6, i





Data storage SQL //TODO delete all currently set alarms if SQL database is changed
SQL Entry -> Alarm Object -> Attached to viewHolder in recyclerView

All UI actions change the Alarm Object
RecyclerView looks at AlarmObject to update the thing
 */
public class MainActivity extends FragmentActivity implements OnItemClick,DialogTimePicker.OnTimeSetListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;


    AlarmEntryDbHelper alarmdb;
    SQLiteDatabase dbWrite;
    AlarmManager alarmManager;
    @Override
    public void onTimeSelected(TimePicker view, int hourOfDay, int minute, int position, int flag, Alarm alarm) {

        //flag - 0 update 1 new 2 delete
        String time = hourOfDay + ":" + minute;
        ContentValues values = new ContentValues();
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT, 30);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR, hourOfDay);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE, minute);

        if (flag == 0) {
            alarm.setHourOfDay(hourOfDay);
            alarm.setMinute(minute);
            alarm.updateAlarmState(dbWrite,alarmManager,this);
            adapter.notifyDataSetChanged();

        } else if (flag == 1) {
            Cursor maxCursor = dbWrite.rawQuery("SELECT MAX(" + AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID + ") FROM " + AlarmEntry.AlarmEntryTable.TABLE_NAME, null);
            int max_UUID_int = 1;
            if (maxCursor.getCount() > 1) {
                System.out.println("Count");
                System.out.println(maxCursor.getCount());

               max_UUID_int = maxCursor.getInt(maxCursor.getColumnIndex(AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID));
                max_UUID_int++;
            }
            values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_UUID, max_UUID_int);
            values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_REPEATING,0);
            values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_DAY,"0000000");
            dbWrite.insert(AlarmEntry.AlarmEntryTable.TABLE_NAME, null, values);
            AlarmManagerHelper.setAlarm(this, alarmManager, hourOfDay, minute, max_UUID_int);
            Log.v("added","");
           
        }
        else if (flag == 2) {
            //TODO Implement update when repeating is enabled
        }
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        adapter.swapCursor(cursor);


    }
    @Override
    public void onItemClicked(int position, View v, Alarm alarm) {
        if (v instanceof TextView) {

            Log.v("Entered :","textview loop");
            int resID = v.getId();
            if (v.isSelected()) {
                v.setSelected(false);
            } else {
                v.setSelected(true);
                v.setBackgroundColor(0xFFFFFF);
            }

            switch(resID) {
                case (R.id.Monday):
                    alarm.updateDay(MONDAY,v.isSelected());

                    break;
                case(R.id.Tuesday) :
                    alarm.updateDay(TUESDAY,v.isSelected());
                    break;
                case(R.id.Wednesday) :
                    alarm.updateDay(WEDNESDAY,v.isSelected());
                    break;
                case(R.id.Thursday) :
                    alarm.updateDay(THURSDAY,v.isSelected());
                    break;
                case(R.id.Friday):
                    alarm.updateDay(FRIDAY,v.isSelected());
                    break;
                case(R.id.Saturday):
                    alarm.updateDay(SATURDAY,v.isSelected());
                    break;
                case(R.id.Sunday):
                    alarm.updateDay(SUNDAY,v.isSelected());
                    break;
                case(R.id.RepeatView):
                    alarm.setRepeating(v.isSelected());
                    break;
                default:
                    DialogTimePicker newFragment = new DialogTimePicker();
                    newFragment.bindAlarm(alarm);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putInt("flag", 0);
                    newFragment.setArguments(bundle);
                    newFragment.show(getFragmentManager(), "timePicker");
                    break;

            }

        } else if (v.getId() == R.id.imbutton) {
            System.out.println("Attempted Removal");
           alarm.removeCompleteAlarmState(dbWrite,alarmManager,this);
            adapter.notifyDataSetChanged();

        }

        alarm.updateAlarmState(dbWrite,alarmManager,this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmdb = new AlarmEntryDbHelper(this);
        dbWrite = alarmdb.getWritableDatabase();

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        //Implement add alarm listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Implement functionality to add new alarm from button
                DialogTimePicker newFragment = new DialogTimePicker();
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                bundle.putInt("flag", 1);
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");


            }
        });


        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        Log.v("in", "onCreate");


        //create Adapter
        adapter = new RecyclerAdapter(this);
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        adapter.setupCursorAdapter(cursor, 0, 0, true); //right 3 parameters are not used
        adapter.setOnItemClick(this);
        recyclerView.setAdapter(adapter);




    }

}
