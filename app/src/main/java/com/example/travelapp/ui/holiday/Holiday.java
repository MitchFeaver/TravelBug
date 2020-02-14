package com.example.travelapp.ui.holiday;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "holiday_table")
public class Holiday {
    @PrimaryKey( autoGenerate = true )
    @NonNull
    @ColumnInfo(name = "ID")

    private int _id ;
    @NonNull
    @ColumnInfo (name = " NAME ")
    private String name ;


    public Holiday (@NonNull String name) {
        this.name = name;
    }

    public void set_id (int id) {
        _id = id;
    }

    public String getName () { return name;
    }

    public int get_id () { return _id;
    }
}