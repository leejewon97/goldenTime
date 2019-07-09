package com.android.ljw.goldentime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SendSosService extends Service
{
    public SendSosService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sendSos", "SendSos Service create");

        Intent inputIntent = new Intent(this, SendSmsService.class);
        inputIntent.putExtra("state", "SOS");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(inputIntent);
        } else {
            startService(inputIntent);
        }

        stopService(new Intent(this, getClass()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("sendSos", "SendSos Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
