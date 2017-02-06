package com.example.secomd.pushupsensor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by secomd on 1/21/2017.
 */

public class AlarmManagerHelper {

    public static void setAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, String UUID) {
        //TODO include functionality for different days
        //get calendar instance at desired time
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        //TODO include uniqueid for each alarm
        //TODO setup unit testing for this class
        Intent intent = new Intent(context, AlarmRecieverActivity.class);
        // intent.putExtra("UUID",UUID);
        PendingIntent pintent = PendingIntent.getActivity(context, UUID.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);

    }

    public static void updateAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, String UUID) {

        //removeAlarm(context, alarmManager, hourOfDay, minute, UUID);
        setAlarm(context, alarmManager, hourOfDay, minute, UUID);

    }

    public static void removeAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, String UUID) {
        Intent intent = new Intent(context, AlarmRecieverActivity.class);
        // intent.putExtra("UUID",UUID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // alarmManager.cancel(pendingIntent);
        // pendingIntent.cancel();
    }
}
