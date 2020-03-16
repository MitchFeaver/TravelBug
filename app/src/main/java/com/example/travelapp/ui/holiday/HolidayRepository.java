package com.example.travelapp.ui.holiday;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HolidayRepository {

    private HolidayDao mHolidayDao;
    private LiveData<List<Holiday>> mAllHolidays;

    HolidayRepository(Application application) {
        HolidayRoomDatabase db = HolidayRoomDatabase.getDatabase(application);
        mHolidayDao = db.holidayDao();
        mAllHolidays = mHolidayDao.getAlphabetizedHolidays();
    }

    public void deleteAll()  {
        new deleteAllHolidaysAsyncTask(mHolidayDao).execute();
    }

    public void deleteHoliday(Holiday holiday)  {
        new deleteHolidaysAsyncTask(mHolidayDao).execute(holiday);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Holiday>> getAllHolidays() {
        return mAllHolidays;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (Holiday holiday) {
        new insertAsyncTask(mHolidayDao).execute(holiday);
    }

    public void update(Holiday holiday) { new UpdateNoteAsyncTask(mHolidayDao).execute(holiday); }

    private static class UpdateNoteAsyncTask extends AsyncTask<Holiday, Void, Void> {
        private HolidayDao holidayDao;

        private UpdateNoteAsyncTask(HolidayDao holidayDao) {
            this.holidayDao = holidayDao;
        }

        @Override
        protected Void doInBackground(Holiday... holidays) {
            holidayDao.update(holidays[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Holiday, Void, Void> {

        private HolidayDao mAsyncTaskDao;

        insertAsyncTask(HolidayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Holiday... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllHolidaysAsyncTask extends AsyncTask<Void, Void, Void> {
        private HolidayDao mAsyncTaskDao;

        deleteAllHolidaysAsyncTask(HolidayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteHolidaysAsyncTask extends AsyncTask<Holiday, Void, Void> {
        private HolidayDao mAsyncTaskDao;

        deleteHolidaysAsyncTask(HolidayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Holiday... params) {
            mAsyncTaskDao.deleteHoliday(params[0]);
            return null;
        }
    }
}