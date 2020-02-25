package com.example.travelapp.ui.holiday;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HolidayDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from holiday_table ORDER BY ID ASC")
    LiveData<List<Holiday>> getAlphabetizedHolidays();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Holiday holiday);

    @Query("DELETE FROM holiday_table")
    void deleteAll();

    @Delete
    void deleteHoliday(Holiday holiday);

    @Query("SELECT * from holiday_table LIMIT 1")
    Holiday[] getAnyHoliday();
}