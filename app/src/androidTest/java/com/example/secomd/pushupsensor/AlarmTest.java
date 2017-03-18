package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Instrumentation;
import android.content.Context;
import android.icu.util.Calendar;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by secomd on 1/21/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AlarmTest {
    @Test
    @TargetApi(24)
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Instrumentation.ActivityMonitor activityMonitor = InstrumentationRegistry.getInstrumentation().addMonitor(CounterView.class.getName(), null, false);

        //AlarmManagerHelper.setAlarm(appContext, alarmManager, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        Activity activity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        //assertNotNull((CounterView) activity);

    }

}
