package com.example.secomd.pushupsensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.PriorityQueue;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

//TODO Exiting this activity without doing desired number of pushups should result in persistent notification and alarm going off
//TODO Test functionality to remove alarm after doing right number of pushups

/*
Activity that senses push-up using the proximity sensor and displays push-up count.
Accelerometer and Gyroscope can be enabled to make sure the phone is not being moved when push-ups need to be done
 */

public class CounterView extends AppCompatActivity{

    PriorityQueue<Float> accelData = new PriorityQueue<Float>();
    PriorityQueue<Float> gyroData = new PriorityQueue<Float>();
    float sum;
    float sum1 = 0;
    int count = 0;
    int maxcount = 30;
    boolean stationary = true;
    int filterWidth = 10;
    float proxThreshold = (float)(filterWidth*20);
    SensorManager sensorManager;
    CounterCircleView counterCircleView;
    boolean mBound;
    AlarmRingtoneService alarmRingtoneService;
    CounterView counterView = this;

    SensorEventListener accelListener = new SensorEventListener() {

    @Override
    public void onSensorChanged(SensorEvent event) {

        sum = 0;

        for(float e : event.values) sum += Math.abs(e);
        accelData.add(new Float(sum));
        if (accelData.size() > 10) accelData.remove();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy) {

    }

};

    SensorEventListener gyroListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            sum = 0;
            for(float e : event.values) sum += Math.abs(e);
            gyroData.add(new Float(sum));
            if (gyroData.size() > 10) gyroData.remove();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor,int accuracy) {

        }

    };

    SensorEventListener proxListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            if( event.values[0] == 3) {
                count += 1;
                counterCircleView.updateProgress(count,maxcount);
            }
            TextView textView = (TextView) findViewById(R.id.textView);

            textView.setText(Integer.toString(count));

            if (count == maxcount) {
                counterView.finish();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor,int accuracy) {

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        Log.d("ok", "what");
        // Get Sensor references
         sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(accelListener,accelSensor,SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener,gyroSensor,SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proxListener,proxSensor,SENSOR_DELAY_NORMAL);
        TextView textView = (TextView) findViewById(R.id.textView);
     counterCircleView = (CounterCircleView) findViewById(R.id.circleView);
       /* FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content_counter_view);
        relativeLayout.addView(counterView,params);
*/
        textView.append(Integer.toString(count));

        //ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar);
        //pBar.setIndeterminate(true);

        //pBar.setProgress(50);


        //alarmRingtoneService.stopRingtone();

        new CountDownTimer(120000, 12000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intermediateIntent = new Intent(counterView, AlarmReceiverActivity.class);
                startActivity(intermediateIntent);
                counterView.finish();
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_counter_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        if (count < maxcount) {
            Intent alarmRecieverIntent = new Intent(this, AlarmReceiverActivity.class);
            startActivity(alarmRecieverIntent);
        }
        super.onStop();
    }

}
