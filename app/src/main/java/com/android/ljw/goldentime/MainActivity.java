package com.android.ljw.goldentime;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    Button btn_num, btn_time, btn_words, btn_sos;
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

    private void init() {
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

        btn_sos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
