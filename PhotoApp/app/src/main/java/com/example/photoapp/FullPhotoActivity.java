package com.example.photoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class FullPhotoActivity extends AppCompatActivity {

    int int_album;
    int currentPhoto, currentIndex;
    ArrayList<Integer> selectedList;
    ImageView ivFullPhoto;
    TextView txtPage;
    Button btnNext,btnPrev;
    GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        txtPage = (TextView) findViewById(R.id.txtPage);
        ivFullPhoto = (ImageView) findViewById(R.id.ivFullPhoto);
        int_album = getIntent().getIntExtra("album", 0);
        selectedList = getIntent().getIntegerArrayListExtra("SelectedList");
        currentIndex = 0;
        currentPhoto = selectedList.get(currentIndex);

        adapter = new GridViewAdapter(this,MainActivity.al_images,int_album);

        adaptPhoto();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == (selectedList.size() - 1)) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                currentPhoto = selectedList.get(currentIndex);

                adaptPhoto();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == 0) {
                    currentIndex = selectedList.size()-1;
                } else {
                    currentIndex--;
                }
                currentPhoto = selectedList.get(currentIndex);

                adaptPhoto();
            }
        });
    }

    private void adaptPhoto() {
        Log.e("currentPhoto",Integer.toString(currentIndex+1));
        Glide.with(getApplicationContext())
                .load("file://" + adapter.al_menu.get(int_album).getAl_photoPath().get(currentPhoto))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivFullPhoto);

        txtPage.setText((currentIndex + 1) + "/" + selectedList.size());
    }
}