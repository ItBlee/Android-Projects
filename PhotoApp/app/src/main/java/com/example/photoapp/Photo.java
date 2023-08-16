package com.example.photoapp;

import java.util.ArrayList;

public class Photo {
    String strAlbum;
    ArrayList<String> al_photoPath;

    public String getStrAlbum() {
        return strAlbum;
    }

    public void setStrAlbum(String strAlbum) {
        this.strAlbum = strAlbum;
    }

    public ArrayList<String> getAl_photoPath() {
        return al_photoPath;
    }

    public void setAl_photoPath(ArrayList<String> al_photoPath) {
        this.al_photoPath = al_photoPath;
    }
}
