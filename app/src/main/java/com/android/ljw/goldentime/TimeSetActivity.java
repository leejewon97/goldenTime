package com.android.ljw.goldentime;

import android.app.TimePickerDialog;
import android.content.Intent;
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
    NumberPicker hourPicker, minutePicker;
    Button btn_start, btn_end, btn_set;

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

        final Calendar calendar = Calendar.getInstance();

        btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(TimeSetActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        // hour, min 가져옴..사용공간
                        String time = hour + "시 " + min + "분";
                        btn_start.setText(time);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
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
                        String time = hour + "시 " + min + "분";
                        btn_end.setText(time);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
                dialog.show();
            }
        });
        btn_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int hour = hourPicker.getValue();
                int min = minutePicker.getValue();
                String time = hour + "시 " + min + "분";
                Toast.makeText(getBaseContext(), time, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("hour", hour);
                intent.putExtra("min", min);
                startActivity(intent);
            }
        });
    }

    public long getTime() {
        int hour = hourPicker.getValue();
        int min = minutePicker.getValue();

        long time = (hour * 3600 + min * 60) * 1000 / 3 * 3;
        return time;
    }
}
