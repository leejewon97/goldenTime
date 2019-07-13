package com.android.ljw.goldentime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

public class ScreenReceiver extends BroadcastReceiver
{
    private static final int SENSE_THRESHOLD = 200;
    long lastTime;
    float lastX, lastY, lastZ;
    private SharedPreferences timeData;
    Boolean checkRinged = false;
    Boolean checkMoVE = true;
    static Boolean checkSet = false;
    static AlarmManager alarmManager;
    static PendingIntent pIntent;
    static long[] dates = new long[6];
    SensorManager sensorManager;
    SensorEventListener sListener;
    TelephonyManager telephonyManager;
    PhoneStateListener pListener;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("testsc", "onReceive");
        Log.e("check", "Ringed : " + checkRinged);
        Log.e("check", "MoVE : " + checkMoVE);
        timeData = context.getSharedPreferences("timeData", MODE_PRIVATE);
        telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) && checkSet) {
            Log.e("testsc", "Screen On");

            pListener = new PhoneStateListener()
            {
                public void onCallStateChanged(int state, String incomingNumber) {
                    if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        Log.e("call", "받음");
                        checkRinged = false;
                        releaseAlarm(context);
                        telephonyManager.listen(pListener, PhoneStateListener.LISTEN_NONE);
                    } else if (state == TelephonyManager.CALL_STATE_IDLE && checkRinged){
                        Log.e("call", "못 받았거나 거절함");
                        telephonyManager.listen(pListener, PhoneStateListener.LISTEN_NONE);
                        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

                        sListener = new SensorEventListener()
                        {
                            @Override
                            public void onSensorChanged(SensorEvent sensorEvent) {
                                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                    long currTime = System.currentTimeMillis();
                                    long gabOfTime = currTime - lastTime;

                                    // 0.1초보다 오래되면 다음을 수행 (100ms)
                                    if (gabOfTime > 100) {
                                        lastTime = currTime;

                                        float currX = sensorEvent.values[0];
                                        float currY = sensorEvent.values[1];
                                        float currZ = sensorEvent.values[2];

                                        // 변위의 절대값에  / gabOfTime * 10000 하여 스피드 계산
                                        float speed = Math.abs(currX + currY + currZ - lastX - lastY - lastZ) / gabOfTime * 10000;
                                        Log.e("speed", "" + speed);

                                        // 임계값보다 크게 움직였을 경우 다음을 수행
                                        if (speed > SENSE_THRESHOLD) {
                                            Log.e("sensor", "sensor sense");
                                            Log.e("speed", "" + speed);
                                            checkMoVE = true;
                                            releaseAlarm(context);
                                            sensorManager.unregisterListener(sListener);
                                            Log.e("sensor", "Sensor Off");
                                        } else {
                                            checkMoVE = false;
                                        }

                                        // 마지막 위치 저장
                                        // m_fLastX = event.values[0]; 그냥 배열의 0번 인덱스가 X값
                                        lastX = sensorEvent.values[0];
                                        lastY = sensorEvent.values[1];
                                        lastZ = sensorEvent.values[2];
                                    }
                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int i) {

                            }
                        };
                        sensorManager.registerListener(sListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
                    }
                }
            };
            telephonyManager.listen(pListener, PhoneStateListener.LISTEN_CALL_STATE);

            if (telephonyManager.getCallState() == TelephonyManager.CALL_STATE_RINGING)
                checkRinged = true;
            else {
                telephonyManager.listen(pListener, PhoneStateListener.LISTEN_NONE);
                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

                sListener = new SensorEventListener()
                {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                            long currTime = System.currentTimeMillis();
                            long gabOfTime = currTime - lastTime;

                            // 0.1초보다 오래되면 다음을 수행 (100ms)
                            if (gabOfTime > 100) {
                                lastTime = currTime;

                                float currX = sensorEvent.values[0];
                                float currY = sensorEvent.values[1];
                                float currZ = sensorEvent.values[2];

                                // 변위의 절대값에  / gabOfTime * 10000 하여 스피드 계산
                                float speed = Math.abs(currX + currY + currZ - lastX - lastY - lastZ) / gabOfTime * 10000;
                                Log.e("speed", "" + speed);

                                // 임계값보다 크게 움직였을 경우 다음을 수행
                                if (speed > SENSE_THRESHOLD) {
                                    Log.e("sensor", "sensor sense");
                                    Log.e("speed", "" + speed);
                                    checkMoVE = true;
                                    releaseAlarm(context);
                                    sensorManager.unregisterListener(sListener);
                                    Log.e("sensor", "Sensor Off");
                                } else {
                                    checkMoVE = false;
                                }

                                // 마지막 위치 저장
                                // m_fLastX = event.values[0]; 그냥 배열의 0번 인덱스가 X값
                                lastX = sensorEvent.values[0];
                                lastY = sensorEvent.values[1];
                                lastZ = sensorEvent.values[2];
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                };
                sensorManager.registerListener(sListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
            }
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.e("testsc", "Screen Off");

            if (checkMoVE && !checkRinged) {
                setAlarm(context, getTime());
                if (timeData.getBoolean("EXCEPT_CHECK", false)) {
                    // 예외시간이 있으면, 계산 서비스 실행
                    Intent service_intent = new Intent(context, CalculateETService.class);
                    service_intent.putExtra("DATES", dates);
                    service_intent.putExtra("ALARM_TIME", getTime());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        context.startForegroundService(service_intent);
                    } else {
                        context.startService(service_intent);
                    }
                }
            } else {
                if (!checkMoVE) {
                    sensorManager.unregisterListener(sListener);
                    Log.e("sensor", "Sensor Off");
                } else if (checkRinged){
                    setAlarm(context, getTime());
                    if (timeData.getBoolean("EXCEPT_CHECK", false)) {
                        // 예외시간이 있으면, 계산 서비스 실행
                        Intent service_intent = new Intent(context, CalculateETService.class);
                        service_intent.putExtra("DATES", dates);
                        service_intent.putExtra("ALARM_TIME", getTime());

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            context.startForegroundService(service_intent);
                        } else {
                            context.startService(service_intent);
                        }
                    }
                }
            }
            // boolean 초기화
            checkMoVE = true;
            checkRinged = false;
        }
    }

    public static void setAlarm(Context context, long time) {
        Log.e("testAL", "setAlarm()");
        dates[0] = System.currentTimeMillis();
        for (int i = 1; i < dates.length; i++) {
            dates[i] = dates[i - 1] + (24 * 60 * 60 * 1000);
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSmsService.class);
        intent.putExtra("state", "ORDINARY");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, dates[0] + time, pIntent);
        checkSet = true;
    }

    private void releaseAlarm(Context context) {
        Log.e("testAL", "releaseAlarm()");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SendSmsService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        alarmManager.cancel(pIntent);
        checkSet = false;
    }

    private long getTime() {
        int hour = timeData.getInt("HOUR", 1);
        int min = timeData.getInt("MIN", 0);

        long time = (hour * 3600 + min * 60) * 1000;

        return time;
    }
}
