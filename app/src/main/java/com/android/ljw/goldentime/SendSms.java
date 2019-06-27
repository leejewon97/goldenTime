package com.android.ljw.goldentime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class SendSms extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        sendSms();
    }

    private void sendSms() {
        Log.e("testal", "send sms");
    }
}
