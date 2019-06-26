package com.android.ljw.goldentime;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendSms extends AppCompatActivity
{
    int start_eHour, start_eMin, end_eHour, end_eMin;
    private SharedPreferences timeData;
    int eDates[][][] = new int[5][2][3];
    long dates[] = ScreenReceiver.dates;
    Boolean notSendCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        Log.e("testal", "send sms");
        timeData = getSharedPreferences("timeData", MODE_PRIVATE);

        if (TimeSetActivity.exceptCheck) {
            eLoad();
            selectDates();
//            send 할때 체크
//            long 추가시간
//            for (예외날들.length)
//                추가시간 += 예외(예외날들[i][0], 예외날들[i][1])
//
//            send 할때 체크
//            If(예외확인 = true) {
//                sendSMS 대신 setAlarm(추가시간)
//            }else
//                sendSms()
            long extraTime = 0;
            for (int i = 0; i < eDates.length; i++)
                extraTime += except(eDates[i][0], eDates[i][1]);

            if (notSendCheck)
                ScreenReceiver.setAlarm(getBaseContext(), extraTime);
            else
                sendSms();
        } else
            sendSms();
    }

    private void sendSms() {
    }

    private long except(int day1[], int day2[]) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(day1[0], day1[1], day1[2], start_eHour, start_eMin);
        calendar2.set(day2[0], day2[1], day2[2], end_eHour, end_eMin);

        long startTime = calendar1.getTimeInMillis();
        long endTime = calendar2.getTimeInMillis();
        long setTime = dates[0];
        long sendTime = System.currentTimeMillis();
        long confTime = 0;

        if (setTime <= startTime) {
            if (sendTime > startTime && sendTime <= endTime) {
                confTime = sendTime - startTime
                        + (endTime - sendTime);
                notSendCheck = true;
            } else if (sendTime > endTime) {
                confTime = endTime - startTime;
                notSendCheck = true;
            }
        } else if (setTime > startTime && setTime <= endTime) {
            if (sendTime > startTime && sendTime <= endTime) {
                confTime = sendTime - setTime
                        + (endTime - sendTime);
                notSendCheck = true;
            } else if (sendTime > endTime) {
                confTime = endTime - setTime;
                notSendCheck = true;
            }
        }

        return confTime;
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
}
