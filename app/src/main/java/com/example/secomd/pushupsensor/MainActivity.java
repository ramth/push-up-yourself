package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
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
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends FragmentActivity implements OnItemClick,DialogTimePicker.OnTimeSetListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[]  myDataset = new String[3];
    AlarmEntryDbHelper alarmdb;

    @Override
    public void onTimeSelected(TimePicker view, int hourOfDay, int minute, int position) {
        String time = hourOfDay + ":" + minute;
        myDataset[position] = time;
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onItemClicked(int position) {
        DialogTimePicker newFragment = new DialogTimePicker();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(),"timePicker");

    }
    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmdb = new AlarmEntryDbHelper(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String time = hour + ":" + minute;
        myDataset[0] = time;
        myDataset[1] = time;
        myDataset[2] = time;
        adapter = new RecyclerAdapter(myDataset);
        adapter.setOnItemClick(this);
        recyclerView.setAdapter(adapter);




    }

}
