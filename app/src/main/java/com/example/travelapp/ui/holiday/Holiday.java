package com.example.travelapp.ui.holiday;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName = "holiday_table")
public class Holiday implements Serializable {
    @PrimaryKey( autoGenerate = true )
    @NonNull
    @ColumnInfo(name = "ID")
    private int _id ;

    @NonNull
    @ColumnInfo (name = " NAME ")
    private String name ;

    @NonNull
    @ColumnInfo(name = " DESCRIPTION ")
    private String holidayMemory;

    @NonNull
    @ColumnInfo(name = " TRAVELBUDDY ")
    private String travelBuddy;

    @NonNull
    @ColumnInfo(name = " STARTDATE ")
    private String startDate;

    @NonNull
    @ColumnInfo(name = " ENDDATE ")
    private String endDate;


    public Holiday (@NonNull String name) { this.name = name; }

    public void set_id (int id) {
        _id = id;
    }

    public int get_id () { return _id; }

    public String getName () { return name; }

    public void setName(String name){
        this.name = name;
    }

    public void setHolidayMemory(String holidayMemory) {
        this.holidayMemory = holidayMemory;
    }

    public String getHolidayMemory() {
        return holidayMemory;
    }

    public void setTravelBuddy(String travelBuddy) {this.travelBuddy = travelBuddy; }

    public String getTravelBuddy() {
        return travelBuddy;
    }

    public void setStartDate(String startDate){ this.startDate = startDate; }

    public String getStartDate(){ return startDate; }

    public void setEndDate(String endDate){ this.endDate = endDate; }

    public String getEndDate(){ return endDate; }

}