package com.example.travelapp.ui.place;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.travelapp.ui.place.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from place_table ORDER BY ID ASC")
    LiveData<List<Place>> getAlphabetizedPlaces();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Place place);

    @Query("DELETE FROM place_table")
    void deleteAll();

    @Delete
    void deletePlace(Place place);

    @Query("SELECT * from place_table LIMIT 1")
    Place[] getAnyPlace();

    @Update
    void update(Place place);
}