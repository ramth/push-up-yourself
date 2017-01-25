package com.example.secomd.pushupsensor;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class AlarmRecieverActivity extends AppCompatActivity {
//TODO add some graphics to this activity

    //TODO trigger a sound
    Context context;
    AlarmRecieverActivity alarmRecieverActivity = this;
    GestureDetector gestureDetector;
    Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO start playing sound
        //TODO button to take you to pushupcounter
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_reciever);
        context = this;
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
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


