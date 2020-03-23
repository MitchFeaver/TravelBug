package com.example.travelapp.ui.holiday;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HolidayViewModel extends AndroidViewModel {

    private HolidayRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Holiday>> mAllHolidays;

    public HolidayViewModel (Application application) {
        super(application);
        mRepository = new HolidayRepository(application);
        mAllHolidays = mRepository.getAllHolidays();
    }

    public LiveData<List<Holiday>> getAllHolidays() { return mAllHolidays; }

    public void insert(Holiday holiday) { mRepository.insert(holiday); }

    public void deleteAll() {mRepository.deleteAll();}

    public void update(Holiday holiday) { mRepository.update(holiday); }

    public void deleteHoliday(Holiday holiday) {
        mRepository.deleteHoliday(holiday);
    }

}