package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

/*
TODO Add a button in viewholder to delete an alarm

 */
public class MainActivity extends FragmentActivity implements OnItemClick,DialogTimePicker.OnTimeSetListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    AlarmEntryDbHelper alarmdb;
    SQLiteDatabase dbWrite;
    @Override
    public void onTimeSelected(TimePicker view, int hourOfDay, int minute, int position, int flag) {

        //flag - 0 update 1 new 2 delete
        String time = hourOfDay + ":" + minute;
        ContentValues values = new ContentValues();
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT, 30);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR, hourOfDay);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE, minute);

        if (flag == 0) {
            dbWrite.update(AlarmEntry.AlarmEntryTable.TABLE_NAME, values, "_id=" + (position + 1), null);
            Log.v("updated", "entry");
        } else if (flag == 1) {
            dbWrite.insert(AlarmEntry.AlarmEntryTable.TABLE_NAME, null, values);
           
        }
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        adapter.swapCursor(cursor);


    }
    @Override
    public void onItemClicked(int position, int flag) {
        if (flag != 2) {
            DialogTimePicker newFragment = new DialogTimePicker();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putInt("flag", flag);
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "timePicker");
        } else {
            dbWrite.delete(AlarmEntry.AlarmEntryTable.TABLE_NAME, "_id=" + (position + 1), null);
            Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms", null);
            adapter.swapCursor(cursor);
            Log.v("deleted", "entry");
        }
    }

    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmdb = new AlarmEntryDbHelper(this);
        dbWrite = alarmdb.getWritableDatabase();
        //Default column
        /*
        ContentValues values = new ContentValues();
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_PUSHUPCOUNT, 30);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_HOUR, 4);
        values.put(AlarmEntry.AlarmEntryTable.COLUMN_NAME_MINUTE, 20);
        long rowid = dbWrite.insert(AlarmEntry.AlarmEntryTable.TABLE_NAME, null, values);
        */
        //SQLiteDatabase dbRead = alarmdb.getReadableDatabase();
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

        /*

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String time = hour + ":" + minute;
        */
        adapter = new RecyclerAdapter(this);
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM Alarms", null);
        adapter.setupCursorAdapter(cursor, 0, 0, true); //right 3 parameters are not used
        adapter.setOnItemClick(this);
        recyclerView.setAdapter(adapter);




    }

}
