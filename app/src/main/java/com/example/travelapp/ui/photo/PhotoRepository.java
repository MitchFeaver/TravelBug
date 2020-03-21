package com.example.travelapp.ui.photo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoRepository {

    private PhotoDao mPhotoDao;
    private LiveData<List<Photo>> mAllPhotos;

    PhotoRepository(Application application) {
        PhotoRoomDatabase db = PhotoRoomDatabase.getDatabase(application);
        mPhotoDao = db.photoDao();
        mAllPhotos = mPhotoDao.getAlphabetizedPhotos();
    }

    public void deleteAll()  {
        new deleteAllPhotosAsyncTask(mPhotoDao).execute();
    }

    public void deletePhoto(Photo photo)  {
        new deletePhotosAsyncTask(mPhotoDao).execute(photo);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Photo>> getAllPhotos() {
        return mAllPhotos;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (Photo photo) {
        new insertAsyncTask(mPhotoDao).execute(photo);
    }

    public void update(Photo photo) { new UpdateNoteAsyncTask(mPhotoDao).execute(photo); }

    private static class UpdateNoteAsyncTask extends AsyncTask<Photo, Void, Void> {
        private PhotoDao photoDao;

        private UpdateNoteAsyncTask(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.update(photos[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Photo, Void, Void> {

        private PhotoDao mAsyncTaskDao;

        insertAsyncTask(PhotoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Photo... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllPhotosAsyncTask extends AsyncTask<Void, Void, Void> {
        private PhotoDao mAsyncTaskDao;

        deleteAllPhotosAsyncTask(PhotoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deletePhotosAsyncTask extends AsyncTask<Photo, Void, Void> {
        private PhotoDao mAsyncTaskDao;

        deletePhotosAsyncTask(PhotoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Photo... params) {
            mAsyncTaskDao.deletePhoto(params[0]);
            return null;
        }
    }
}