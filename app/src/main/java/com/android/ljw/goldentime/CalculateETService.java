package com.android.ljw.goldentime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalculateETService extends Service
{
    private SharedPreferences timeData;
    int start_eHour, start_eMin, end_eHour, end_eMin;
    int eDates[][][] = new int[5][2][3];
    long[] dates = new long[6];
    long alarmTime;

    public CalculateETService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("calculateET", "calculateET Service create");
        
        Notification notification = new NotificationCompat.Builder(this, "").build();
        startForeground(90, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("calculateET", "calculateET Service startCommand");
        //여기서 작업
        dates = intent.getLongArrayExtra("DATES");
        alarmTime = intent.getLongExtra("ALARM_TIME", 0);
        eLoad();
        selectDates();

        long extraTime = 0;
        for (int i = 0; i < eDates.length; i++)
            extraTime += except(eDates[i][0], eDates[i][1]);
        for (int i = 0; i < eDates.length; i++)
            extraTime += lastCheck(extraTime, eDates[i][0], eDates[i][1]);

        Log.e("extraTime in calculate", "" + extraTime);
        ScreenReceiver.setAlarm(this, alarmTime + extraTime);

        stopService(intent);
        return super.onStartCommand(intent, flags, startId);
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
        long sendTime = dates[0] + alarmTime;
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
    private long lastCheck(long eTime, int day1[], int day2[]) {
        long checkTime = dates[0] + alarmTime + eTime;
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
        timeData = getSharedPreferences("timeData", MODE_PRIVATE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("calculateET", "calculateET Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
