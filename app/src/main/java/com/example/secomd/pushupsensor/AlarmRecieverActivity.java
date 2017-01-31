package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.lang.annotation.Target;

public class AlarmRecieverActivity extends AppCompatActivity {
//TODO add some graphics to this activity
//TODO exiting intermediate activity without calling pushup counter should not terminate alarm
//TODO move ringtone generation to a service and allow access from AlarmRecieverActivity and CounterView
    Context context;
    AlarmRecieverActivity alarmRecieverActivity = this;
    GestureDetector gestureDetector;
    Ringtone r;

    @Override
    protected void onStop() {
        makeNotification(this);
        super.onStop();
    }

    @TargetApi(23)
    private void makeNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(this, notification);
        if (r != null) {
            Log.v("not", "null");
        } else {
            Log.v("is", "null");
        }
        r.play();
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
            r.stop();
            alarmRecieverActivity.finish();

            return true;
        }

    }
}


