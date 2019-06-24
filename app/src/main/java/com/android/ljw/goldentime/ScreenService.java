package com.android.ljw.goldentime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ScreenService extends Service
{
    private BroadcastReceiver mReceiver;
//    private SharedPreferences timeData;
//    static int hour, min;

    public ScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("testsc", "ScreenService create");
        Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("testsc", "ScreenService startCommand");

//        if (intent == null) {
//            Log.e("testsc", "intent == null");
//
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//            mReceiver = new ScreenReceiver();
//            registerReceiver(mReceiver, intentFilter);
//        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
