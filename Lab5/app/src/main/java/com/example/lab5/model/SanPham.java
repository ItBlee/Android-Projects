package com.example.lab5.model;

public class SanPham {
    private int id;
    private  String  tenSP;
    private  int giaSP;
    private  String hinhSP;

    public SanPham() {

    }

    public SanPham(String ten, int gia, String hinh) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }

    public SanPham(int id, String tenSP, int giaSP, String hinhSP) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
    }
}
