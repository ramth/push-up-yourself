package com.example.secomd.pushupsensor;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
/*
Service that starts/stops ringtone
 */
public class AlarmRingtoneService extends Service {

    private Ringtone r;
    private final IBinder mBinder = new LocalBinder();
    private boolean ringtoneState = false;

    public class LocalBinder extends Binder {

        public AlarmRingtoneService getService() {
            Log.v("service", "onBind");
            return AlarmRingtoneService.this;
        }
    }

    public void startRingtone() {
        if (!ringtoneState) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(this, notification);
            if (r != null) {
                Log.v("not", "null");
            } else {
                Log.v("is", "null");
            }
            r.play();
            ringtoneState = true;
        }
    }


    public void stopRingtone() {
        if (ringtoneState) {
            r.stop();
            ringtoneState = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("service", "onBind");
        // TODO: Return the communication channel to the service.
        return mBinder;
    }


}
