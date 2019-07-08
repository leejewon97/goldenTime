package com.android.ljw.goldentime;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SendSosService extends Service
{
    private SharedPreferences numData, wordsData;

    public SendSosService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sendSos", "SendSos Service create");

        Notification notification = new NotificationCompat.Builder(this, "").build();
        startForeground(5678, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("sendSos", "SendSos Service startCommand");
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

        String msg = wordsData.getString("WORDS_SOS", "");
        if (msg.equals("")) {
            msg = "기본 긴급 문자.";
        }
        if (!numData.getString("SOS", "").equals("")) {
            msg += " 위치 조회 : http://bit.ly/2XmxpLI";
            smsManager.sendTextMessage(numData.getString("SOS", ""), null, msg, null, null);
            Log.e("sendSms", "SendSms to " + numData.getString("SOS", ""));
            Toast.makeText(this, numData.getString("SOS", "") + "님께 문자를 전송하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("sendSos", "SendSos Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
