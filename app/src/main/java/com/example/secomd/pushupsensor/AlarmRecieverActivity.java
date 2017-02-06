package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Icon;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.lang.annotation.Target;

public class AlarmRecieverActivity extends AppCompatActivity {
//TODO add some graphics to this activity
//TODO Service doesn't persist after exiting from recent apps list
    Context context;
    AlarmRecieverActivity alarmRecieverActivity = this;
    AlarmRingtoneService alarmRingtoneService;
    GestureDetector gestureDetector;
    NotificationManager notificationManager;
    Ringtone r;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder mBinder) {
            Log.v("in", "onServiceConnected");
            alarmRingtoneService = ((AlarmRingtoneService.LocalBinder) mBinder).getService();
            alarmRingtoneService.startRingtone();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };
    @Override
    protected void onStop() {

        super.onStop();
    }

    @TargetApi(23)
    private void makeNotification(Context context) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, AlarmRecieverActivity.class);
        //does id matter?
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Icon icon = Icon.createWithResource(context, R.drawable.ic_access_alarms);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Alarm")
                .setContentText("Go to Alarm Activity")
                .setContentIntent(pendingIntent)
                .setSmallIcon(icon);


        Notification n;

        n = builder.build();

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(1, n);
        //PendingIntent pendingIntent = PendingIntent.get
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_reciever);
        context = this;

        Intent serviceIntent = new Intent(getBaseContext(), AlarmRingtoneService.class);
        startService(serviceIntent);
        Log.v("bindSuccess", Boolean.toString(bindService(serviceIntent, mConnection, BIND_AUTO_CREATE)));
        Log.v("mBound", Boolean.toString(mBound));
        makeNotification(this);
        gestureDetector = new GestureDetector(this, new GestureListener());


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            //Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Intent intent = new Intent(context, CounterView.class);
            startActivity(intent);
            alarmRingtoneService.stopRingtone();
            notificationManager.cancel(1);
            unbindService(mConnection);
            alarmRecieverActivity.finish();
            return true;
        }

    }
}


