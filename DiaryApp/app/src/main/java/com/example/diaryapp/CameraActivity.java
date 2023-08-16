package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    Button btnTakePic,btnTakeVid,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnTakePic = findViewById(R.id.btnTakePic);
        btnTakeVid = findViewById(R.id.btnTakeVid);
        btnCancel = findViewById(R.id.btnCancel);

        try {
            if (getIntent().getStringExtra("isMedia").equals("false"))
                btnCancel.setEnabled(false);
        } catch (NullPointerException ignored) {}

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        btnTakeVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("removeMedia");
            }
        });
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                try {
                    returnResult(saveImage(imageBitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "Hủy chụp ảnh", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Lỗi chụp ảnh", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    returnResult(saveVideo(data.getData()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "Hủy quay phim", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Lỗi quay phim", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void returnResult(String value) {
        Intent data = new Intent();
        data.putExtra("CameraResult", value);
        if (value != null && !value.equals("removeMedia") && !value.isEmpty())
            data.putExtra("Location", getLocation());
        setResult(RESULT_OK, data);
        finish();
    }

    private String saveImage(Bitmap bitmap) throws Exception{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp +".jpg";
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsoluteFile() + "/DiaryApp");

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

        CameraActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return filePath;
    }

    private String saveVideo(Uri uri) throws Exception {
        File newfile;

        AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(uri, "r");
        FileInputStream in = videoAsset.createInputStream();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "VID_" + timeStamp +".mp4";
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsoluteFile() + "/DiaryApp");

        if(!file.exists()) {
            if(!file.mkdirs()) {
                throw new Exception("Không thể tạo thư mục, " + file.getAbsolutePath());
            }
        }

        newfile = new File(file, fileName);
        if (newfile.exists()) newfile.delete();

        FileOutputStream out = new FileOutputStream(newfile);

        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();

        return newfile.getPath();
    }

    @SuppressLint("MissingPermission")
    private String getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String cityname = "";

        try {
            List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
            if (address.size() > 0) {
                for (Address adr : address) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0)
                        cityname = adr.getLocality();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityname;
    }
}