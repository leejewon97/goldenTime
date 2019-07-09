package com.android.ljw.goldentime;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    public static final String CHANNEL_ID = "default";
    Button btn_num, btn_time, btn_words, btn_sos, btn_power;
    public static final int REQ_SMS = 999;
    private long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, REQ_SMS);
            } else
                init();
        } else
            init();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                init();
            else {
                Toast.makeText(this, "권한을 허용하지 않으면 앱을 사용 하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void init() {
        createNotificationChannel();

        setContentView(R.layout.activity_main);

        btn_num = findViewById(R.id.btn_num);
        btn_time = findViewById(R.id.btn_time);
        btn_words = findViewById(R.id.btn_words);
        btn_sos = findViewById(R.id.btn_sos);
        btn_power = findViewById(R.id.btn_power);

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

        btn_sos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SosSetActivity.class);
                startActivity(intent);
            }
        });

        btn_power.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (ScreenService.power_switch) {
                    Intent service_intent = new Intent(getBaseContext(), ScreenService.class);
                    stopService(service_intent);
                    Toast.makeText(getBaseContext(), "꺼짐", Toast.LENGTH_SHORT).show();
                } else {
                    Intent service_intent = new Intent(getBaseContext(), ScreenService.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        startForegroundService(service_intent);
                    } else {
                        startService(service_intent);
                    }
                    Toast.makeText(getBaseContext(), "켜짐", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
