package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver
{
    AlarmManager alarmManager;
    PendingIntent pIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("testsc", "onReceive");

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.e("testsc", "Screen On");
            Toast.makeText(context, "Screen On", Toast.LENGTH_SHORT).show();
            //화면이 켜지면, 알람을 끔
//            releaseAlarm(context);
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("testsc", "Screen Off");
            Toast.makeText(context, "Screen Off", Toast.LENGTH_SHORT).show();
            // 화면이 꺼지면, 알람을 켬
//            setAlarm(context, 5000);
        }
        if (intent.getAction().equals(".intent_gogo")) {
            Log.e("testsc", "intent gogo");
            Toast.makeText(context, "intent gogo", Toast.LENGTH_SHORT).show();

            Intent service_intent = new Intent(context, ScreenService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
                Log.e("testsc", "ForegroundService");
            } else {
                context.startService(service_intent);
                Log.e("testsc", "Service");
            }
        }
    }


    private void setAlarm(Context context, long time) {
        Log.e("testal", "setAlarm()");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(context, SendSms.class);
        pIntent = PendingIntent.getActivity(context, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pIntent);
    }

    private void releaseAlarm(Context context) {
        Log.e("testal", "releaseAlarm()");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(context, SendSms.class);
        pIntent = PendingIntent.getActivity(context, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pIntent);
    }
}
