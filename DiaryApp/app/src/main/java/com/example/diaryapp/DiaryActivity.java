package com.example.diaryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Date;

public class DiaryActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 123;
    private static String id = null;
    private static String title = null;
    private static String content = null;
    private static String weather = null;
    private static String filePath = null;
    private static String location = null;

    Diary diary = new Diary();

    EditText txtTitle,txtContent,txtLocation;
    Button btnCancel,btnSave,btnMedia;
    ImageView ivMedia;
    Database db;

    MaterialButtonToggleGroup toggleButtonGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        db = new Database(this);

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        txtLocation = findViewById(R.id.txtLocation);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnMedia = findViewById(R.id.btnMedia);
        ivMedia = findViewById(R.id.ivMedia);
        toggleButtonGroup = findViewById(R.id.toggleButtonGroup);

        Intent intent = getIntent();
        id = intent.getStringExtra("listId");
        if (id != null) {
            title = intent.getStringExtra("Title");
            content = intent.getStringExtra("Content");
            weather = intent.getStringExtra("Weather");
            filePath = intent.getStringExtra("FilePath");
            location = intent.getStringExtra("Location");

            txtTitle.setText(title);
            txtContent.setText(content);
            txtLocation.setText(location);

            Glide.with(getApplicationContext())
                    .load(filePath)
                    .placeholder(R.drawable.media)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivMedia);
            ivMedia.setBackgroundColor(Color.TRANSPARENT);

            switch (weather) {

                case "sunlow":
                    toggleButtonGroup.check(R.id.btnCenter);
                    break;

                case "rain":
                    toggleButtonGroup.check(R.id.btnRight);
                    break;

                case "snow":
                    toggleButtonGroup.check(R.id.btnJustified);
                    break;

                default:
                    toggleButtonGroup.check(R.id.btnLeft);
                    break;
            }
        } else {
            toggleButtonGroup.check(R.id.btnLeft);
            weather = "sunshine";
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiary();
                backToMain();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, CameraActivity.class);
                if (filePath == null)
                    intent.putExtra("isMedia","false");
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        ivMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath == null) {
                    Toast.makeText(getApplicationContext(),"Chưa có ảnh hay video !",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DiaryActivity.this, MediaActivity.class);
                    intent.putExtra("MediaPath", filePath);
                    startActivity(intent);
                }
            }
        });

        toggleButtonGroup.setSelectionRequired(true);
        toggleButtonGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {

                    case R.id.btnCenter:
                        weather = "sunlow";
                        break;

                    case R.id.btnRight:
                        weather = "rain";
                        break;

                    case R.id.btnJustified:
                        weather = "snow";
                        break;

                    default:
                        weather = "sunshine";
                        break;
                }
            }
        });

    }

    public void setDiary() {
        String datetime = (String) DateFormat.format("dd/MM/yyyy  hh:mm:ss", new Date());
        title = txtTitle.getText().toString();
        content = txtContent.getText().toString();
        location = txtLocation.getText().toString();

        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(),"Tựa đề trống !",Toast.LENGTH_SHORT).show();
            return;
        }

        diary.setTitle(title);
        diary.setContent(content);
        diary.setDateTime(datetime);
        diary.setWeather(weather);
        diary.setFilePath(filePath);
        diary.setLocation(location);

        if (id != null) {
            diary.setID(id);
            if (db.updateDiary(diary))
                Toast.makeText(getApplicationContext(),"Đã lưu chỉnh sửa", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Lưu thất bại", Toast.LENGTH_SHORT).show();
        } else {
            long l = -1;
            l = db.insertDiary(diary);
            if(l>=0)
                Toast.makeText(getApplicationContext(),"Đã thêm nhật ký", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Chưa thêm nhật ký", Toast.LENGTH_SHORT).show();
        }
        backToMain();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            try {
                filePath = data.getStringExtra("CameraResult");
                txtLocation.setText(data.getStringExtra("Location"));
                if (filePath.equals("removeMedia")) {
                    filePath = null;
                    Toast.makeText(getApplicationContext(),
                            "Đã hủy ảnh/video", Toast.LENGTH_LONG)
                            .show();
                    ivMedia.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.media));
                    ivMedia.setBackgroundColor(Color.LTGRAY);
                } else {
                    Toast.makeText(getApplicationContext(),
                            filePath, Toast.LENGTH_LONG)
                            .show();
                    Glide.with(getApplicationContext())
                            .load(filePath)
                            .placeholder(R.drawable.media)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivMedia);
                    ivMedia.setBackgroundColor(Color.TRANSPARENT);
                }
            } catch (NullPointerException ignored) {
            }
        }
    }

    public void backToMain() {
        Intent intent = new Intent(DiaryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
