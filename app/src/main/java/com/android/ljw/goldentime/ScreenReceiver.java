package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ScreenReceiver extends BroadcastReceiver
{
    private SharedPreferences timeData;
    Boolean setCheck = false;
    AlarmManager alarmManager;
    PendingIntent pIntent;
    long[] dates = new long[6];
    int eDates[][][] = new int[5][2][3];
    int start_eHour, start_eMin, end_eHour, end_eMin;


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
            if (timeData.getBoolean("EXCEPT_CHECK", false)) {
                setAlarm(context, getTime() + calculateExcept());
            }
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

    private long calculateExcept() {
        eLoad();
        selectDates();

        long extraTime = 0;
        for (int i = 0; i < eDates.length; i++)
            extraTime += except(eDates[i][0], eDates[i][1]);
        for (int i = 0; i < eDates.length; i++)
            extraTime += lastCheck(extraTime, eDates[i][0], eDates[i][1]);

        Log.e("extraTime", "" + extraTime);
        Log.e("getTime", "" + getTime());
        return extraTime;
    }

    private long lastCheck(long eTime, int day1[], int day2[]) {
        long checkTime = dates[0] + getTime() + eTime;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        // set(month : 0~11)
        calendar1.set(day1[0], day1[1] - 1, day1[2], start_eHour, start_eMin);
        calendar2.set(day2[0], day2[1] - 1, day2[2], end_eHour, end_eMin);

        long startTime = calendar1.getTimeInMillis();
        long endTime = calendar2.getTimeInMillis();
        if (checkTime >= startTime && checkTime < endTime)
            return endTime - startTime;
        else
            return 0;
    }

    private void eLoad() {
        start_eHour = timeData.getInt("START_E_HOUR", 0);
        start_eMin = timeData.getInt("START_E_MIN", 0);
        end_eHour = timeData.getInt("END_E_HOUR", 0);
        end_eMin = timeData.getInt("END_E_MIN", 0);
    }

    private void selectDates() {
        SimpleDateFormat yFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat MFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dFormat = new SimpleDateFormat("dd");
        SimpleDateFormat[] formats = {yFormat, MFormat, dFormat};

        if (start_eHour < end_eHour || (start_eHour == end_eHour && start_eMin < end_eMin)) {
            // 시작시각 < 끝시각
            for (int i = 0; i < eDates.length; i++) {
                for (int j = 0; j < eDates[i].length; j++) {
                    for (int k = 0; k < eDates[i][j].length; k++) {
                        eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i]));
                    }
                }
            }
        } else if (start_eHour > end_eHour || (start_eHour == end_eHour && start_eMin > end_eMin)) {
            // 시작시각 > 끝시각
            int set_hour, set_min;
            SimpleDateFormat HFormat = new SimpleDateFormat("HH");
            SimpleDateFormat mFormat = new SimpleDateFormat("mm");
            set_hour = Integer.parseInt(HFormat.format(dates[0]));
            set_min = Integer.parseInt(mFormat.format(dates[0]));

            if (set_hour < end_eHour || (set_hour == end_eHour && set_min <= end_eMin)) {
                // set시각 <= 끝시각
                for (int i = 0; i < eDates.length; i++) {
                    for (int j = 0; j < eDates[i].length; j++) {
                        for (int k = 0; k < eDates[i][j].length; k++) {
                            if (j == 0) {
                                if (i == 0) {
                                    // dates[-1] 처리 (-1일)
                                    eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[0] - (24 * 60 * 60 * 1000)));
                                } else
                                    eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i - 1]));
                            } else if (j == 1) {
                                eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i]));
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < eDates.length; i++) {
                    for (int j = 0; j < eDates[i].length; j++) {
                        for (int k = 0; k < eDates[i][j].length; k++) {
                            if (j == 0) {
                                eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i]));
                            } else if (j == 1) {
                                eDates[i][j][k] = Integer.parseInt(formats[k].format(dates[i + 1]));
                            }
                        }
                    }
                }
            }
        }
    }

    private long except(int day1[], int day2[]) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        // set(month : 0~11)
        calendar1.set(day1[0], day1[1] - 1, day1[2], start_eHour, start_eMin);
        calendar2.set(day2[0], day2[1] - 1, day2[2], end_eHour, end_eMin);

        long startTime = calendar1.getTimeInMillis();
        long endTime = calendar2.getTimeInMillis();
        long setTime = dates[0];
        long sendTime = dates[0] + getTime();
        long confTime = 0;
//        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
//        Log.e("except", "start : " + Format.format(startTime) + ", end : " + Format.format(endTime) + ", set : " + Format.format(setTime) + ", send : " + Format.format(sendTime));

        if (setTime <= startTime && !(sendTime < startTime)) {
            Log.e("except", "case1,2");
            confTime = endTime - startTime;
        } else if (setTime > startTime && setTime <= endTime) {
            Log.e("except", "case3,4");
            confTime = endTime - setTime;
        }

        Log.e("except", "confTime : " + confTime);
        return confTime;
    }

    public void setAlarm(Context context, long time) {
        Log.e("testAL", "setAlarm()");
        dates[0] = System.currentTimeMillis();
        for (int i = 1; i < dates.length; i++) {
            dates[i] = dates[i - 1] + (24 * 60 * 60 * 1000);
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSms.class);
        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, dates[0] + time, pIntent);
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

        long time = (hour * 3600 + min * 60) * 1000;
//        Log.e("getTIme", hour + "시간 " + min + "분");

        return time;
    }
}
