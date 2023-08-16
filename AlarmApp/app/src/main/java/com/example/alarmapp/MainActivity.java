package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    Button btnTimePicker, btnStop;
    ListView lvAlarmList;

    //adapter cho listView để adap danh sách vào listView hiển thị
    ArrayAdapter<String> arrayAdapter;

    // lưu dữ liệu bằng SharedPreferences theo kiểu key-value
    SharedPreferences sharedPreferences;

    // DS báo thức
    ArrayList<String> alarmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // xử lý load dữ liệu đã lưu
            sharedPreferences = getSharedPreferences("AlarmAppDB", MODE_PRIVATE);
            Set<String> alarmSet = sharedPreferences.getStringSet("alarmSet", null);
            alarmList.addAll(alarmSet);
        } catch(NullPointerException ignored) {}

        btnTimePicker = (Button) findViewById(R.id.btnTimepicker);
        btnStop = (Button) findViewById(R.id.btnStop);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, alarmList);
        lvAlarmList.setAdapter(arrayAdapter);

        //click nút chọn báo thức mới có id btnTimepicker
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimePickerActivity.class);
                // gửi DS báo thức qua intent tới TimePickerActivity
                intent.putStringArrayListExtra("AlarmList", alarmList);
                startActivity(intent);
                finish();
            }
        });

        //click nút dừng báo thức mới có id btnStop
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                intent.putExtra("turnMusic","off");
                sendBroadcast(intent);
            }
        });

        lvAlarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int alarmToDelete = position;
                // To delete the data from the App
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Bạn chắc chứ?")
                        .setMessage("Bạn muốn xóa giờ báo thức này ?")
                        .setPositiveButton("Đúng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int alarmToCancel = Integer.parseInt(alarmList.get(alarmToDelete).replace(":",""));
                                alarmList.remove(alarmToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                Set<String> alarmSet = new HashSet<>(alarmList);
                                sharedPreferences.edit().putStringSet("alarmSet", alarmSet).apply();
                                cancelAlarm(alarmToCancel);
                            }
                        }).setNegativeButton("Không", null).show();
            }
        });
    }

    // xóa báo thức đã đặt
    private void cancelAlarm(int alarmToCancel) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmToCancel, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}