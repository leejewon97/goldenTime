package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    Button btn_num, btn_time, btn_words, btn_sos;

//    static TimerTask mTask;
//    private Timer mTimer;

//    long setedTime = 360000;
//    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendBroadcast(new Intent(".intent_gogo"));
        Log.e("testsc", "send_broadcast");

        setContentView(R.layout.activity_main);

        btn_num = findViewById(R.id.btn_num);
        btn_time = findViewById(R.id.btn_time);
        btn_words = findViewById(R.id.btn_words);
        btn_sos = findViewById(R.id.btn_sos);

        btn_num.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NumSetActivity.class);
                startActivity(intent);
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TimeSetActivity.class);
                startActivity(intent);
            }
        });
        btn_words.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), WordsSetActivity.class);
                startActivity(intent);
            }
        });

//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//
//        registerReceiver(screenOnOff, intentFilter);

//        mTask = new TimerTask() {
//            @Override
//            public void run() {
////                Log.e("test","전송");
////                Toast.makeText(MainActivity.this,"위험 문자 전송",Toast.LENGTH_SHORT).show();
//                count++;
//            }
//        };
//        mTimer = new Timer(true);
    }

//   public TimerTask redefTask(){
//        TimerTask tempTask = new TimerTask()
//        {
//            @Override
//            public void run() {
//                count++;
//                Log.e("test카운트",String.valueOf(count));
//            }
//        };
//        return tempTask;
//    }

//    BroadcastReceiver screenOnOff = new BroadcastReceiver()
//    {
//        public static final String ScreenOff = "android.intent.action.SCREEN_OFF";
//        public static final String ScreenOn = "android.intent.action.SCREEN_ON";
//
//        public void onReceive(Context context, Intent intent)
//        {
//            if (Objects.equals(intent.getAction(), ScreenOff))
//            {
//                Log.e("testsc", "Screen Off");
//                //count 시작... 도달하면, sms 전송
////                mTask = redefTask();
////                mTimer.schedule(mTask,0,1000);
//
//            }
//            else if (Objects.equals(intent.getAction(), ScreenOn))
//            {
//                Log.e("testsc", "Screen On");
//                //count = 0
////                mTask.cancel();
////                count = 0;
////                Log.e("test카운트",String.valueOf(count));
//            }
//        }
//    };
//
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        unregisterReceiver(screenOnOff);
//    }
}
