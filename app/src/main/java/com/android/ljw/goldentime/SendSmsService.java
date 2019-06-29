package com.android.ljw.goldentime;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

public class SendSmsService extends Service
{
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
    public void sendSMS()
    {
        Log.e("sendSms", "SendSms");
        SmsManager smsManager = SmsManager.getDefault();
        String number = "01077071019";
        String msg = "test";
        smsManager.sendTextMessage(number, null, msg, null, null);
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
