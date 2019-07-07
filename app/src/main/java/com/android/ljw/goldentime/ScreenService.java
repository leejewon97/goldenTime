package com.android.ljw.goldentime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ScreenService extends Service
{
    private BroadcastReceiver mReceiver;
    public static boolean power_switch;

    public ScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("testsc", "ScreenService create");
        Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();
        power_switch = true;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("testsc", "ScreenService startCommand");

        Intent NotiIntent = new Intent(this, SendSmsService.class);
        NotiIntent.putExtra("state", "SOS");
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(this, 0, NotiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getService(this, 0, NotiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID) //CHANNEL_ID 채널에 지정한 아이디
                .setContentTitle(" SOS <긴급 문자 보내기>")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();

        startForeground(1234, notification);
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
        Log.e("testsc", "ScreenService Destroy");
        Toast.makeText(getBaseContext(), "sc service destroy", Toast.LENGTH_SHORT).show();
        power_switch = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
