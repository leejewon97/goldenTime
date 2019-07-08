package com.android.ljw.goldentime;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

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

        Notification notification = new NotificationCompat.Builder(this, "").build();
        startForeground(5678, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("sendSms", "SendSms Service startCommand");
        //여기서 작업
        sendSMS();
        stopService(intent);
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
            SimpleDateFormat HFormat = new SimpleDateFormat("HH");
            SimpleDateFormat mFormat = new SimpleDateFormat("mm");
            long time = System.currentTimeMillis() - ScreenReceiver.dates[0];
            if (time >= 24 * 60 * 60 * 1000)
                msg = HFormat.format(time) + "시간 " + mFormat.format(time) + "분 동안 핸드폰을 사용하지 않았습니다.";
            else
                msg = mFormat.format(time) + "분 동안 핸드폰을 사용하지 않았습니다.";
            Log.e("time,msg", time + ", " + msg);
        }


        for (int i = 0; i < 5; i++) {
            numbers[i] = numData.getString(key[i], "");
            if (!numbers[i].equals("")) {
                msg += " 위치 조회 : http://bit.ly/2XmxpLI";
                smsManager.sendTextMessage(numbers[i], null, msg, null, null);
                Log.e("sendSms", "SendSms to " + numbers[i]);
                Toast.makeText(this, numbers[i] + "님께 문자를 전송하였습니다.", Toast.LENGTH_SHORT).show();
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
