package com.example.photoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPhotoActivity extends AppCompatActivity {
    File file;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(NewPhotoActivity.this,
                    Manifest.permission.CAMERA))) {

            } else {
                ActivityCompat.requestPermissions(NewPhotoActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        100);
            }
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        Toast.makeText(NewPhotoActivity.this, "Không được cấp quyền ! Thử lại", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void takePhoto() {
        Intent cameraImgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraImgIntent, 1100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {
                saveToFileAndUri(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"Hủy chụp ảnh", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Lỗi chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        Intent intent = new Intent(NewPhotoActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void saveToFileAndUri(Bitmap bitmap) throws Exception{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "PhotoApp_" + timeStamp +".jpg";
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsoluteFile() + "/PhotoApp");

        if(!file.exists()) {
            if(!file.mkdirs()) {
                throw new Exception("Không thể tạo thư mục, " + file.getAbsolutePath());
            }
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream output = new FileOutputStream(filePath);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        
        output.flush();
        output.close();

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(6);
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.DATE_ADDED, timeStamp);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.SIZE, size);

        NewPhotoActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}