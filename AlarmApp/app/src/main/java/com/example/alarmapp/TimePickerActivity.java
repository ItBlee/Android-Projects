package com.example.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class TimePickerActivity extends AppCompatActivity {

    Button btnCreate,btnCancel;
    TimePicker timePicker;
    Calendar calendar;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        ArrayList<String> alarmList = getIntent().getStringArrayListExtra("AlarmList");

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        calendar = Calendar.getInstance();

        //click nút tạo báo thức mới có id btnCreate
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                // xét thời gian ví dụ 7h > 7:00 nên phải format 07:00
                String hour = String.valueOf(timePicker.getCurrentHour());
                String minute = String.valueOf(timePicker.getCurrentMinute());

                if (timePicker.getCurrentHour() < 10) //số time bé hơn 10 thì thêm 0 vào trước
                    hour = "0" + hour;
                if (timePicker.getCurrentMinute() < 10)
                    minute = "0" + minute;

                String time = hour + ":" + minute;
                //xét if cái thời gian báo thức ông đặt đã có chưa
                if (!alarmList.contains(time)) {
                    alarmList.add(time);
                    Set<String> alarmSet = new HashSet<>(alarmList);
                    SharedPreferences.Editor editor = getSharedPreferences("AlarmAppDB", MODE_PRIVATE).edit();
                    editor.putStringSet("alarmSet", alarmSet);
                    editor.apply();
                    int alarmToStart = Integer.parseInt(time.replace(":",""));
                    startAlarm(calendar, alarmToStart);
                    finishTimePicker();
                } else {
                    Toast.makeText(TimePickerActivity.this, "Giờ này đã được đặt trước đó! Thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //click nút hủy báo thức mới có id btnCancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTimePicker();
            }
        });
    }

    private void finishTimePicker() {
        Intent intent = new Intent(TimePickerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //phương thức để khởi tạo báo thức theo thời gian alarmToStart
    private void startAlarm(Calendar c, int alarmToStart) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("turnMusic","on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmToStart, intent, 0);
        //Nếu mà thời gian đặt đã qua rồi thì nó đặt tăng 1 ngày tức là ngày mai
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        //đặt báo thức lặp lại theo mili giây
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
    }
}