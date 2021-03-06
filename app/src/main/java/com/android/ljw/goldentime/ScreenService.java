package com.android.ljw.goldentime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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

        Intent notiIntent = new Intent(this, SendSosService.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getService(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        PendingIntent openIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID) //CHANNEL_ID 채널에 지정한 아이디
                .setContentTitle("SOS  <긴급 문자 보내기>")
                .setContentText("앱을 실행 하려면 밑에 '열기'를 눌러주세요.")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_stat_golden_time)
                .setContentIntent(pendingIntent)
                .addAction(0, "열기", openIntent)
                .setOngoing(true)
                .build();

        startForeground(1234, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Log.e("testsc", "ScreenService Destroy");
//        Toast.makeText(getBaseContext(), "ScreenService Destroy", Toast.LENGTH_SHORT).show();
        power_switch = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
