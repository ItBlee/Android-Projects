package com.example.photoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {
    int int_album;
    private GridView gridView;
    GridViewAdapter adapter;
    Button btnTakePic;

    ArrayList<Integer> selectedList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedList = new ArrayList<>();
        btnTakePic = (Button) findViewById(R.id.btnTakePic);
        btnTakePic.setText("Xem ảnh");

        gridView = (GridView)findViewById(R.id.gv_folder);
        int_album = getIntent().getIntExtra("value", 0);
        adapter = new GridViewAdapter(this,MainActivity.al_images,int_album);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedList.contains(position)) {
                    selectedList.add(position);
                    view.setBackgroundColor(Color.CYAN);
                } else {
                    selectedList.remove(Integer.valueOf(position));
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                Log.e("List",selectedList.toString());
            }
        });

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList.isEmpty()) {
                    for (int i = 0;i < adapter.getCount();i++) {
                        selectedList.add(i);
                    }
                }

                Intent intent = new Intent(PhotosActivity.this, FullPhotoActivity.class);
                intent.putExtra("album", int_album);
                intent.putIntegerArrayListExtra("SelectedList",selectedList);
                startActivity(intent);
            }
        });
    }
}