package com.example.travelapp.ui.photo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {

    private PhotoRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Photo>> mAllPhotos;

    public PhotoViewModel (Application application) {
        super(application);
        mRepository = new PhotoRepository(application);
        mAllPhotos = mRepository.getAllPhotos();
    }

    LiveData<List<Photo>> getAllPhotos() { return mAllPhotos; }

    public void insert(Photo photo) { mRepository.insert(photo); }

    public void deleteAll() {mRepository.deleteAll();}

    public void update(Photo photo) { mRepository.update(photo); }

    public void deletePhoto(Photo photo) {
        mRepository.deletePhoto(photo);
    }
}