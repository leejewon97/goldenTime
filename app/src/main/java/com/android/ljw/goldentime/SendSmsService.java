package com.android.ljw.goldentime;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class SendSmsService extends Service
{
    private SharedPreferences numData, wordsData;
    String numbers[] = new String[5];
    String key[] = {"TEXT1", "TEXT2", "TEXT3", "TEXT4", "TEXT5"};

    public SendSmsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sendSms", "SendSms Service create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("sendSms", "SendSms Service startCommand");
        //여기서 작업
        sendSMS(intent);
        stopService(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendSMS(Intent intent) {
        Log.e("sendSms", "SendSms");

        numData = getSharedPreferences("numData", MODE_PRIVATE);
        wordsData = getSharedPreferences("wordsData", MODE_PRIVATE);

        SmsManager smsManager = SmsManager.getDefault();

        if (intent.getStringExtra("state").equals("ORDINARY")) {
            ScreenReceiver.checkSet = false;

            String msg = wordsData.getString("WORDS", "");
            if (msg.equals("")) {
                long minute = (System.currentTimeMillis() - ScreenReceiver.dates[0]) / (60 * 1000);
                //setAlarm부터 sendSms까지의 시간을 분으로 환산
                String hour = String.valueOf(minute / 60);
                String min = String.valueOf(minute % 60);
                //그 시간들을 시간과 분으로 나눠서 저장
                if (minute >= 60) {
                    if (min.equals("0"))
                        msg = hour + "시간 동안 핸드폰을 사용하지 않았습니다.";
                    else
                        msg = hour + "시간 " + min + "분 동안 핸드폰을 사용하지 않았습니다.";
                } else
                    msg = min + "분 동안 핸드폰을 사용하지 않았습니다.";
                Log.e("time", minute + ", " + msg);
            }

            for (int i = 0; i < 5; i++) {
                numbers[i] = numData.getString(key[i], "");
                if (!numbers[i].equals("")) {
                    msg += " 위치 조회 : http://bit.ly/2XmxpLI";
                    smsManager.sendTextMessage(numbers[i], null, msg, null, null);
                    Log.e("sendSms", "SendSms to " + numbers[i]);
                } else
                    break;
            }
        } else if (intent.getStringExtra("state").equals("SOS")) {
            String msg = wordsData.getString("WORDS_SOS", "");
            if (msg.equals("")) {
                msg = "기본 긴급 문자.";
            }
            if (!numData.getString("SOS", "").equals("")) {
                msg += " 위치 조회 : http://bit.ly/2XmxpLI";
                smsManager.sendTextMessage(numData.getString("SOS", ""), null, msg, null, null);
                Log.e("sendSms", "SendSms to " + numData.getString("SOS", ""));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("sendSms", "SendSms Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
