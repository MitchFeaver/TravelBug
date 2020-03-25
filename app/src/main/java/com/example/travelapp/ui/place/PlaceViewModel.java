package com.example.travelapp.ui.place;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.travelapp.ui.place.Place;
import com.example.travelapp.ui.place.PlaceRepository;

import java.util.List;

public class PlaceViewModel extends AndroidViewModel {

    private PlaceRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Place>> mAllPlaces;

    public PlaceViewModel(Application application) {
        super(application);
        mRepository = new PlaceRepository(application);
        mAllPlaces = mRepository.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() { return mAllPlaces; }

    public void insert(Place place) { mRepository.insert(place); }

    public void deleteAll() {mRepository.deleteAll();}

    public void update(Place place) { mRepository.update(place); }

    public void deletePlace(Place place) {
        mRepository.deletePlace(place);
    }

}