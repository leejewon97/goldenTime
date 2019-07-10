package com.android.ljw.goldentime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) ||
                Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            if (intent.getDataString().contains("com.android.ljw.goldentime")) {
//                패키지를 지정해야 이 앱에서만 설치시나 업데이트를 인지함
                Log.e("testsc", "package");

                Intent service_intent = new Intent(context, ScreenService.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    context.startForegroundService(service_intent);
                    Log.e("testsc", "ForegroundService");
                } else {
                    context.startService(service_intent);
                    Log.e("testsc", "Service");
                }
            }
        }
    }
}
