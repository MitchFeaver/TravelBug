package com.example.travelapp.ui.photo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotoDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from photo_table ORDER BY ID ASC")
    LiveData<List<Photo>> getAlphabetizedPhotos();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Photo photo);

    @Query("DELETE FROM photo_table")
    void deleteAll();

    @Delete
    void deletePhoto(Photo photo);

    @Query("SELECT * from photo_table LIMIT 1")
    Photo[] getAnyPhoto();

    @Update
    void update(Photo photo);
}
