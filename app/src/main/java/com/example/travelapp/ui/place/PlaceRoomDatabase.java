package com.example.travelapp.ui.place;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.travelapp.ui.place.Place;
import com.example.travelapp.ui.place.PlaceDao;

@Database(entities = {Place.class}, version = 1,  exportSchema = false)
public abstract class PlaceRoomDatabase extends RoomDatabase {

    public abstract PlaceDao placeDao();

    private static PlaceRoomDatabase INSTANCE;

    static PlaceRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlaceRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlaceRoomDatabase.class, "place_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static Callback sRoomDatabaseCallback = new Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PlaceDao mDao;
        String [] places = {"PlaceSample1"};

        PopulateDbAsync(PlaceRoomDatabase db) {
            mDao = db.placeDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            // If we have no places, then create the sample list
            if (mDao.getAnyPlace().length < 1) {
                for (int i = 0; i <= places.length - 1; i++) {
                    Place place = new Place(places[i]);
                    place.setLocation("London Eye");
                    place.setPlaceMemory("This is a sample memory");
                    place.setDate("1/1/19");
                    mDao.insert(place);
                }
            }
            return null;
        }
    }
}
