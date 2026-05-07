package com.universidad.inmobiliaria.ui.inmuebles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InmueblesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InmueblesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is inmuebles fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}