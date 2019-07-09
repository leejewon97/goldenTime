package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

public class ScreenReceiver extends BroadcastReceiver
{
    private SharedPreferences timeData;
    Boolean checkRinged = false;
    static Boolean checkSet = false;
    static AlarmManager alarmManager;
    static PendingIntent pIntent;
    static long[] dates = new long[6];

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("testsc", "onReceive");
        timeData = context.getSharedPreferences("timeData", MODE_PRIVATE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.e("testsc", "BOOT");

            Intent service_intent = new Intent(context, ScreenService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(service_intent);
                Log.e("testsc", "ForegroundService");
            } else {
                context.startService(service_intent);
                Log.e("testsc", "Service");
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON) && checkSet) {
            Log.e("testsc", "Screen On");

            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

            if (telephonyManager.getCallState() == TelephonyManager.CALL_STATE_RINGING)
                checkRinged = true;
            else
                releaseAlarm(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("testsc", "Screen Off");
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);

            if (!checkRinged) {
                setAlarm(context, getTime());
                if (timeData.getBoolean("EXCEPT_CHECK", false)) {
                    Intent service_intent = new Intent(context, CalculateETService.class);
                    service_intent.putExtra("DATES", dates);
                    service_intent.putExtra("ALARM_TIME", getTime());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(service_intent);
                    } else {
                        context.startService(service_intent);
                    }
                }
            } else
                checkRinged = false;
        }
    }

    public static void setAlarm(Context context, long time) {
        Log.e("testAL", "setAlarm()");
        dates[0] = System.currentTimeMillis();
        for (int i = 1; i < dates.length; i++) {
            dates[i] = dates[i - 1] + (24 * 60 * 60 * 1000);
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSmsService.class);
        intent.putExtra("state", "ORDINARY");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, dates[0] + time, pIntent);
        checkSet = true;
    }

    private void releaseAlarm(Context context) {
        Log.e("testAL", "releaseAlarm()");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSmsService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.cancel(pIntent);
        checkSet = false;
    }

    private long getTime() {
        int hour = timeData.getInt("HOUR", 1);
        int min = timeData.getInt("MIN", 0);

        long time = (hour * 3600 + min * 60) * 1000;

        return time;
    }

    PhoneStateListener listener = new PhoneStateListener()
    {
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                checkRinged = false;
                Log.e("call", "받음");
            }
        }
    };
}
