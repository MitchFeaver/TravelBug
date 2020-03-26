package com.example.travelapp.ui.photo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName = "photo_table")
public class Photo implements Serializable {
    @PrimaryKey( autoGenerate = true )
    @NonNull
    @ColumnInfo(name = "ID")
    private int _id ;

    @NonNull
    @ColumnInfo (name = " NAME ")
    private String photoName ;

    @NonNull
    @ColumnInfo (name = " HOLIDAY ")
    private String holidayName ;

    @NonNull
    @ColumnInfo (name = " PHOTOURL ")
    private String photoURL ;

    @NonNull
    @ColumnInfo (name = " PHOTOLOC ")
    private String photoLocation ;

    public Photo (@NonNull String photoName) { this.photoName = photoName; }

    public void set_id (int id) {
        _id = id;
    }

    public int get_id () { return _id; }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName){
        this.holidayName = holidayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String imageURL) {
        this.photoURL = imageURL;
    }

    @NonNull
    public String getPhotoLocation() {
        return photoLocation;
    }

    public void setPhotoLocation(@NonNull String photoLocation) {
        this.photoLocation = photoLocation;
    }
}