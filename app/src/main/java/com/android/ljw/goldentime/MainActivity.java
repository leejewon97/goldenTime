package com.android.ljw.goldentime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{

    Button btn_num, btn_time, btn_txt, btn_sos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_num = findViewById(R.id.btn_num);
        btn_time = findViewById(R.id.btn_time);
        btn_txt = findViewById(R.id.btn_txt);
        btn_sos = findViewById(R.id.btn_sos);

        btn_num.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NumSetActivity.class);
                // .class는 왜 하는가
                startActivity(intent);
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TimeSetActivity.class);
                // .class는 왜 하는가
                startActivity(intent);
            }
        });
        btn_txt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NumSetActivity.class);
                // .class는 왜 하는가
                startActivity(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        registerReceiver(screenOnOff, intentFilter);
    }

    BroadcastReceiver screenOnOff = new BroadcastReceiver()
    {
        public static final String ScreenOff = "android.intent.action.SCREEN_OFF";
        public static final String ScreenOn = "android.intent.action.SCREEN_ON";

        public void onReceive(Context contex, Intent intent)
        {
            if (Objects.equals(intent.getAction(), ScreenOff))
            {
                Log.e("testsc", "Screen Off");
                //count 시작... 도달하면, sms 전송
            }
            else if (Objects.equals(intent.getAction(), ScreenOn))
            {
                Log.e("testsc", "Screen On");
                //count = 0
            }
        }
    };

    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(screenOnOff);
    }
}
