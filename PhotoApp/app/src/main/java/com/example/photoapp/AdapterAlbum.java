package com.example.photoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;

public class AdapterAlbum extends ArrayAdapter<Photo> {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Photo> al_menu;

    public AdapterAlbum(Context context, ArrayList<Photo> al_menu) {
        super(context, R.layout.adapter_album, al_menu);
        this.al_menu = al_menu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_album, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.txtFolder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.txtFolder2);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setText(al_menu.get(position).getStrAlbum());
        viewHolder.tv_foldersize.setText(al_menu.get(position).getAl_photoPath().size() + "");

        Glide.with(context).load("file://" + al_menu.get(position).getAl_photoPath().get(0))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
    }
}