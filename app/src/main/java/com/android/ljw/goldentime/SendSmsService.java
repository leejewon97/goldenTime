package com.android.ljw.goldentime;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;

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
        onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("sendSms", "SendSms Service startCommand");
        //여기서 작업
        sendSMS();
        onDestroy();
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendSMS() {
        Log.e("sendSms", "SendSms");
        ScreenReceiver.checkSet = false;

        numData = getSharedPreferences("numData", MODE_PRIVATE);
        wordsData = getSharedPreferences("wordsData", MODE_PRIVATE);

        SmsManager smsManager = SmsManager.getDefault();
        String msg = wordsData.getString("WORDS", "");
        if (msg.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH시간 mm분");
            long time = System.currentTimeMillis() - ScreenReceiver.dates[0];
            msg = simpleDateFormat.format(time) + " 동안 핸드폰을 사용하지 않았습니다.";
        }

        for (int i = 0; i < 5; i++) {
            numbers[i] = numData.getString(key[i], "");
            if (!numbers[i].equals("")) {
                smsManager.sendTextMessage(numbers[i], null, msg, null, null);
                Log.e("sendSms", "SendSms to " + numbers[i]);
            } else
                break;
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
