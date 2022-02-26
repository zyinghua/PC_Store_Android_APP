package com.example.labw3.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class PCViewModel extends AndroidViewModel {
    private PCRepository pcRepository;
    private LiveData<List<PC>> allPCs;
    private LiveData<Integer> numOfPCs;

    public PCViewModel(@NonNull Application application) {
        super(application);
        this.pcRepository = new PCRepository(application);
        this.allPCs = this.pcRepository.getAllPCs();
        this.numOfPCs = this.pcRepository.getNumOfPCs();
    }

    public LiveData<List<PC>> getAllPCs() {
        return this.allPCs;
    }

    public LiveData<Integer> getNumOfPCs() {return this.numOfPCs;}

    public void insert(PC pc) {
        this.pcRepository.insert(pc);
    }

    public void deleteAll() {
        this.pcRepository.deleteAll();
    }

    public void deleteLastPC() {
        this.pcRepository.deleteLastPC();
    }

}
