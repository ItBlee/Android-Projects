package com.example.lab5.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab5.R;
import com.example.lab5.model.SanPham;

import java.util.ArrayList;

public class SanPhamAdapter  extends BaseAdapter {
    public SanPhamAdapter(ArrayList<SanPham> alSanPham, Context context) {
        this.alSanPham = alSanPham;
        this.context = context;
    }

    private ArrayList<SanPham> alSanPham = new ArrayList<SanPham>();
    private Context context;
    @Override
    public int getCount() {
        return alSanPham.size();
    }

    @Override
    public Object getItem(int i) {
        return alSanPham.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) alSanPham.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.item_sanpham, viewGroup, false);
        }

        TextView txtTenSP = view.findViewById(R.id.txtTenSP);
        TextView txtGiaSP = view.findViewById(R.id.txtGia);
        ImageView hinhSP = view.findViewById(R.id.hinhSP);

        txtTenSP.setText(alSanPham.get(i).getTenSP());
        txtGiaSP.setText(String.valueOf(alSanPham.get(i).getGiaSP()));

        int id = context.getResources().getIdentifier(alSanPham.get(i).getHinhSP(), "mipmap", context.getPackageName());
        hinhSP.setImageResource(id);
        return view;
    }
}
