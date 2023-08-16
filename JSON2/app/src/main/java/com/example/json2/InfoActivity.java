package com.example.json2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity {

    Intent intent;

    TextView detail;
    ImageView flagImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        detail = (TextView) findViewById(R.id.detail);
        flagImage = (ImageView) findViewById(R.id.flagImage);

        intent = getIntent();
        String flagImageUrl = "https://img.geonames.org/flags/x/" + intent.getStringExtra("CountryCode").toLowerCase() + ".gif";

        detail.setText(intent.getStringExtra("Detail"));
        Glide.with(this).load(flagImageUrl).into(flagImage);
    }
}