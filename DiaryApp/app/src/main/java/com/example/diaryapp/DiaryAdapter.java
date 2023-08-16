package com.example.diaryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URLConnection;
import java.util.ArrayList;

public class DiaryAdapter extends ArrayAdapter<Diary> {
    Context context;
    ArrayList<Diary> ItemList;

    public DiaryAdapter(@NonNull Context context, ArrayList<Diary> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent,false);
        }

        Diary item = ItemList.get(position);
        if (item.getWeather() == null) item.setWeather("");
        if (item.getFilePath() == null) item.setFilePath("");

        TextView lvItemTitleID = convertView.findViewById(R.id.lvItemTitleID);
        TextView lvItemDateID = convertView.findViewById(R.id.lvItemDateID);
        TextView lvItemLocationID = convertView.findViewById(R.id.lvItemLocationID);
        ImageView ivWeather = convertView.findViewById(R.id.ivWeather);
        ImageView ivMedia = convertView.findViewById(R.id.ivMedia);

        String title = item.getTitle();
        String date = item.getDateTime();
        String location = item.getLocation();

        lvItemTitleID.setText(title);
        lvItemDateID.setText(date);

        if (location == null || location.isEmpty())
            lvItemLocationID.setVisibility(View.GONE);
        else lvItemLocationID.setText(location);

        switch (item.getWeather()) {
            case "sunlow":
                ivWeather.setImageDrawable(context.getResources().getDrawable(R.drawable.sunlow));
                break;

            case "rain":
                ivWeather.setImageDrawable(context.getResources().getDrawable(R.drawable.rain));
                break;

            case "snow":
                ivWeather.setImageDrawable(context.getResources().getDrawable(R.drawable.snow));
                break;

            default:
                ivWeather.setImageDrawable(context.getResources().getDrawable(R.drawable.sunshine));
                break;
        }

        if (item.getFilePath().isEmpty())
            ivMedia.setVisibility(View.INVISIBLE);
        if (isVideoFile(item.getFilePath()))
            ivMedia.setImageDrawable(context.getResources().getDrawable(R.drawable.video));

        return convertView;
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }
}
