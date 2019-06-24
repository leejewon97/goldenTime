package com.android.ljw.goldentime;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimeSetActivity extends AppCompatActivity
{
    private SharedPreferences timeData;
    static boolean exceptCheck;
    int start_eHour, start_eMin, end_eHour, end_eMin;
    NumberPicker hourPicker, minutePicker;
    Button btn_start, btn_end, btn_set;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_set);

        hourPicker = findViewById(R.id.hours);
        minutePicker = findViewById(R.id.minutes);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        btn_set = findViewById(R.id.btn_set);

        hourPicker.setMinValue(0);
        //테스트용으로 잠시 최솟값을 0으로 낮춤
        hourPicker.setMaxValue(100);
        hourPicker.setWrapSelectorWheel(false);
        hourPicker.setOnLongPressUpdateInterval(100);
//        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
//        {
//            @Override
//            public void onValueChange(NumberPicker numberPicker, int old, int now) {
//                String msg = String.format("%d시간", now);
//                Toast.makeText(TimeSetActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        });

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setOnLongPressUpdateInterval(100);
//        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
//        {
//            @Override
//            public void onValueChange(NumberPicker numberPicker, int old, int now) {
//                String msg = String.format("%d분", now);
//                Toast.makeText(TimeSetActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        });

        calendar = Calendar.getInstance();

        timeData = getSharedPreferences("timeData", MODE_PRIVATE);
        load();
//
//        hourPicker.setValue(hour);
//        minutePicker.setValue(min);
//        btn_start.setText(start);
//        btn_end.setText(end);

        btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(TimeSetActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        // hour, min 가져옴..사용공간
                        start_eHour = hour;
                        start_eMin = min;

                        btn_start.setText(start_eHour + "시 " + start_eMin + "분");
                    }
                }, start_eHour, start_eMin, false);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
                dialog.show();
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(TimeSetActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        // hour, min 가져옴..사용공간
                        end_eHour = hour;
                        end_eMin = min;

                        btn_end.setText(end_eHour + "시 " + end_eMin + "분");
                    }
                }, end_eHour, end_eMin, false);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
                dialog.show();
            }
        });
        btn_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (start_eHour == end_eHour && start_eMin == end_eMin) {
                    exceptCheck = false;
                    Toast.makeText(getBaseContext(), "예외시간이 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else if (btn_start.getText().equals("시작 시간") || btn_end.getText().equals("끝나는 시간")){
                    exceptCheck = false;
                    Toast.makeText(getBaseContext(), "예외시간이 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    exceptCheck = true;
                    Toast.makeText(getBaseContext(), "예외시간 : " + btn_start.getText() + " ~ " + btn_end.getText(), Toast.LENGTH_SHORT).show();
                }

                save();

                onBackPressed();
            }
        });
    }

    private void save() {
        SharedPreferences.Editor editor = timeData.edit();

        editor.putInt("HOUR", hourPicker.getValue());
        editor.putInt("MIN", minutePicker.getValue());
        if (exceptCheck) {
            editor.putString("START", btn_start.getText().toString());
            editor.putString("END", btn_end.getText().toString());

            editor.putInt("START_E_HOUR", start_eHour);
            editor.putInt("START_E_MIN", start_eMin);
            editor.putInt("END_E_HOUR", end_eHour);
            editor.putInt("END_E_MIN", end_eMin);
        } else {
            editor.remove("START");
            editor.remove("END");

            editor.remove("START_E_HOUR");
            editor.remove("START_E_MIN");
            editor.remove("END_E_HOUR");
            editor.remove("END_E_MIN");
        }

        editor.apply();
    }

    private void load() {
        hourPicker.setValue(timeData.getInt("HOUR", 1));
        minutePicker.setValue(timeData.getInt("MIN", 0));
        btn_start.setText(timeData.getString("START", btn_start.getText().toString()));
        btn_end.setText(timeData.getString("END", btn_end.getText().toString()));

        start_eHour = timeData.getInt("START_E_HOUR", calendar.get(Calendar.HOUR_OF_DAY));
        start_eMin = timeData.getInt("START_E_MIN", calendar.get(Calendar.MINUTE));
        end_eHour = timeData.getInt("END_E_HOUR", calendar.get(Calendar.HOUR_OF_DAY));
        end_eMin = timeData.getInt("END_E_MIN", calendar.get(Calendar.MINUTE));
    }
}
