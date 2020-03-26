package com.example.travelapp.ui.photo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.travelapp.ui.photo.Photo;
import com.example.travelapp.ui.photo.PhotoDao;

@Database(entities = {Photo.class}, version = 1,  exportSchema = false)
public abstract class PhotoRoomDatabase extends RoomDatabase {

    public abstract PhotoDao photoDao();

    private static PhotoRoomDatabase INSTANCE;

    static PhotoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PhotoRoomDatabase.class, "photo_database")
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

        private final PhotoDao mDao;
        String [] photos = {"PhotoSample1"};

        PopulateDbAsync(PhotoRoomDatabase db) {
            mDao = db.photoDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            // If we have no photos, then create the sample list
            if (mDao.getAnyPhoto().length < 1) {
                for (int i = 0; i <= photos.length - 1; i++) {
                    Photo photo = new Photo(photos[i]);
                    photo.setHolidayName("Barcelona");
                    photo.setPhotoURL("");
                    photo.setPhotoLocation("Agonda");
                    mDao.insert(photo);
                }
            }
            return null;
        }
    }
}
