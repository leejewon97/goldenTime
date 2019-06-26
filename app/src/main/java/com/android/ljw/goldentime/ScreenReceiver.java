package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ScreenReceiver extends BroadcastReceiver
{
    private SharedPreferences timeData;
    Boolean setCheck = false;
    static AlarmManager alarmManager;
    static PendingIntent pIntent;
    static long[] dates = new long[6];

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("testsc", "onReceive");
        timeData = context.getSharedPreferences("timeData", MODE_PRIVATE);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON) && setCheck) {
            Log.e("testsc", "Screen On");
            Toast.makeText(context, "Screen On", Toast.LENGTH_SHORT).show();
            //화면이 켜지면, 알람을 끔
            releaseAlarm(context);
            setCheck = false;
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("testsc", "Screen Off");
            Toast.makeText(context, "Screen Off", Toast.LENGTH_SHORT).show();
            // 화면이 꺼지면, 알람을 켬
            setAlarm(context, getTime());

            setCheck = true;
        }
        if (intent.getAction().equals(".intent_gogo")) {
            Log.e("testsc", "intent gogo");

//            Toast.makeText(context, "intent gogo", Toast.LENGTH_SHORT).show();

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

    public static void setAlarm(Context context, long time) {
        Log.e("testAL", "setAlarm()");
        dates[0] = System.currentTimeMillis();
        for (int i = 1; i < dates.length; i++) {
            dates[i] = dates[i - 1] + (24 * 60 * 60 * 1000);
        }
//        SimpleDateFormat sdformat = new SimpleDateFormat("yy년 MM월 dd일, HH시 mm분 ss초 ");
//        for (int i = 0; i < 6; i++) {
//            Log.e("날짜", "dates[" + i + "] : " + sdformat.format(dates[i]));
//        }

//        SimpleDateFormat yFormat = new SimpleDateFormat("yy");
//        SimpleDateFormat MFormat = new SimpleDateFormat("MM");
//        SimpleDateFormat dFormat = new SimpleDateFormat("dd");
//        SimpleDateFormat[] formats = {yFormat, MFormat, dFormat};
//        for (int i = 0; i < eDates.length; i++) {
//            for (int j = 0; j < eDates[i].length; j++) {
//                for (int k = 0; k < eDates[i][j].length; k++) {
//                    if (j == 0) {
//                        if (i == 0) {
//                            // dates[-1] 처리 (-1일)
//                            eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[0] - (24 * 60 * 60 * 1000)));
//                        } else
//                            eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i - 1]));
//                    } else if (j == 1) {
//                        eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i]));
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < eDates.length; i++) {
//            for (int j = 0; j < eDates[i].length; j++) {
//                for (int k = 0; k < eDates[i][j].length; k++) {
//                    Log.e("array", "eDates[" + i + "][" + j + "][" + k + "] : " + eDates[i][j][k]);
//                }
//            }
//        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSms.class);
        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pIntent);
    }

    private void releaseAlarm(Context context) {
        Log.e("testAL", "releaseAlarm()");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(context, SendSms.class);
        pIntent = PendingIntent.getActivity(context, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pIntent);
    }

    private long getTime() {
        int hour = timeData.getInt("HOUR", 1);
        int min = timeData.getInt("MIN", 0);

        long time = (hour * 3600 + min * 60) * 1000 / 3 * 3;
        Log.e("testAL", hour + "시 " + min + "분");

        return time;
    }
}
