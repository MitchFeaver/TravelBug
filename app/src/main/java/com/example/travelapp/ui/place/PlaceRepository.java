package com.example.travelapp.ui.place;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.travelapp.ui.place.Place;
import com.example.travelapp.ui.place.PlaceDao;
import com.example.travelapp.ui.place.PlaceRoomDatabase;

import java.util.List;

public class PlaceRepository {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;

    PlaceRepository(Application application) {
        PlaceRoomDatabase db = PlaceRoomDatabase.getDatabase(application);
        mPlaceDao = db.placeDao();
        mAllPlaces = mPlaceDao.getAlphabetizedPlaces();
    }

    public void deleteAll()  {
        new deleteAllPlacesAsyncTask(mPlaceDao).execute();
    }

    public void deletePlace(Place place)  {
        new deletePlacesAsyncTask(mPlaceDao).execute(place);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Place>> getAllPlaces() {
        return mAllPlaces;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (Place place) {
        new insertAsyncTask(mPlaceDao).execute(place);
    }

    public void update(Place place) { new UpdateNoteAsyncTask(mPlaceDao).execute(place); }

    private static class UpdateNoteAsyncTask extends AsyncTask<Place, Void, Void> {
        private PlaceDao placeDao;

        private UpdateNoteAsyncTask(PlaceDao placeDao) {
            this.placeDao = placeDao;
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeDao.update(places[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao mAsyncTaskDao;

        insertAsyncTask(PlaceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Place... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllPlacesAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlaceDao mAsyncTaskDao;

        deleteAllPlacesAsyncTask(PlaceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deletePlacesAsyncTask extends AsyncTask<Place, Void, Void> {
        private PlaceDao mAsyncTaskDao;

        deletePlacesAsyncTask(PlaceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Place... params) {
            mAsyncTaskDao.deletePlace(params[0]);
            return null;
        }
    }
}