package com.example.travelapp.ui.place;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

@Entity( tableName = "place_table")
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int _id;

    @NonNull
    @ColumnInfo(name = " NAME ")
    private String name;

    @NonNull
    @ColumnInfo(name = " DESCRIPTION ")
    private String placeMemory;

    @NonNull
    @ColumnInfo(name = " LOCATION ")
    private String location;

    @NonNull
    @ColumnInfo(name = " DATE ")
    private String date;

    @NonNull
    @ColumnInfo(name = " PLACEHOLIDAY ")
    private String placeHoliday;

    @NonNull
    @ColumnInfo(name = " IMAGE ")
    private String image;

    @NonNull
    @ColumnInfo(name = " LATITUDE ")
    private Double longitude;

    @NonNull
    @ColumnInfo(name = " LONGITUDE ")
    private Double latitude;

    public Place(@NonNull String name) {
        this.name = name;
    }

    public void set_id(int id) {
        _id = id;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlaceMemory(String placeMemory) {
        this.placeMemory = placeMemory;
    }

    public String getPlaceMemory() {
        return placeMemory;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setPlaceHoliday(String placeHoliday) {
        this.placeHoliday = placeHoliday;
    }

    @NonNull
    public String getPlaceHoliday() {
        return placeHoliday;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude(){
        return longitude;
    }
}

