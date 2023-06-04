package com.example.fitnessapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Integer> mProgress;

    public HomeViewModel() {
        mProgress = new MutableLiveData<>();
        mProgress.setValue(0);
    }

    public LiveData<Integer> getProgress() {
        return mProgress;
    }

    public void setmProgress(Integer progress) {
        mProgress.setValue(progress);
    }
}