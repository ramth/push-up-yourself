package com.example.secomd.pushupsensor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;

/**
 * Created by secomd on 1/21/2017.
 */

public class AlarmManagerHelper {

    public static void setAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, int UUID) {
        //TODO include functionality for different days
        //get calendar instance at desired time
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        //TODO include uniqueid for each alarm
        //TODO setup unit testing for this class
        Intent intent = new Intent(context, AlarmReceiverActivity.class);
        // intent.putExtra("UUID",UUID);
        PendingIntent pintent = PendingIntent.getActivity(context, UUID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);

    }

    public static void setRepeatingAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, int day, int UUID) {
        Calendar alarmDate = Calendar.getInstance();
        alarmDate.set(Calendar.HOUR_OF_DAY, hourOfDay);

        alarmDate.set(Calendar.MINUTE, minute);
        Calendar nowDate = Calendar.getInstance();

        int currentDay = nowDate.get(DAY_OF_WEEK);

        if( currentDay == day ) {
            if(nowDate.after(alarmDate)) {
                //get next week
                alarmDate.set(DAY_OF_WEEK,day);
                alarmDate.add(DATE,7);
            }
            else {
                //
                alarmDate.set(DAY_OF_WEEK,day);
            }
        }
        else if (currentDay > day) {
            //day has occured in current week
            //gets to next week
            alarmDate.set(DAY_OF_WEEK,day);
            alarmDate.add(DATE,7);
        }
        else {
            //day has not occured in current week
            alarmDate.set(DAY_OF_WEEK, day);
        }

        System.out.println("Alarm Date is " + alarmDate.getTime().toString());
        System.out.println("It is now" + nowDate.getTime().toString());

        Intent intent = new Intent(context, AlarmReceiverActivity.class);
        intent.addCategory(Integer.toString(day));
        // intent.putExtra("UUID",UUID);
        PendingIntent pintent = PendingIntent.getActivity(context,UUID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmDate.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, pintent);
    }
    public static void updateAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, int UUID) {

        //removeAlarm(context, alarmManager, hourOfDay, minute, UUID);
        setAlarm(context, alarmManager, hourOfDay, minute, UUID);

    }

    public static void removeAlarm(Context context, AlarmManager alarmManager, int hourOfDay, int minute, int UUID) {
        Intent intent = new Intent(context, AlarmReceiverActivity.class);
        // intent.putExtra("UUID",UUID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // alarmManager.cancel(pendingIntent);
        // pendingIntent.cancel();
    }

    public static void removeAlarmRepeating(Context context, AlarmManager alarmManager, int hourOfDay, int minute, int UUID) {
        for (int day = 0; day < 7 ; day ++) {
            Intent intent = new Intent(context, AlarmReceiverActivity.class);
            intent.addCategory(Integer.toString(day));
            // intent.putExtra("UUID",UUID);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }
}
