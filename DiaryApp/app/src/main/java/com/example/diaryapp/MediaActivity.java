package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URLConnection;

public class MediaActivity extends AppCompatActivity {

    ImageView ivFullPhoto;
    VideoView ivFullVideo;

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        ivFullPhoto = findViewById(R.id.ivFullPhoto);
        ivFullVideo = findViewById(R.id.ivFullVideo);

        if (this.mediaController == null) {
            this.mediaController = new MediaController(MediaActivity.this);

            this.mediaController.setAnchorView(ivFullVideo);

            this.ivFullVideo.setMediaController(mediaController);
        }

        String mediaPath = getIntent().getStringExtra("MediaPath");

        if (isVideoFile(mediaPath)) {
            ivFullPhoto.setVisibility(View.GONE);
            ivFullVideo.setVisibility(View.VISIBLE);

            ivFullVideo.setVideoPath(mediaPath);
            ivFullVideo.requestFocus();
            ivFullVideo.start();
        } else {
            Glide.with(getApplicationContext())
                    .load(mediaPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivFullPhoto);
        }
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
}