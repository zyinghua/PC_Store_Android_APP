package com.example.labw3.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.List;

public class PCRepository {
    private PCDao pcDao;
    private LiveData<List<PC>> allPCs;
    private LiveData<Integer> numOfPCs;

    PCRepository(Application application) {
        PCDatabase db = PCDatabase.getDatabase(application);
        pcDao = db.pcDao();
        allPCs = pcDao.getAllPCs();
        numOfPCs = pcDao.getNumOfPCs();
    }

    LiveData<List<PC>> getAllPCs() {
        return this.allPCs;
    }

    void insert(PC pc) {
        PCDatabase.databaseWriteExecutor.execute(() -> pcDao.addPC(pc));
    }

    void deleteAll() {
        PCDatabase.databaseWriteExecutor.execute(() -> pcDao.deleteAllPC());
    }

    void deleteLastPC() {
        PCDatabase.databaseWriteExecutor.execute(() -> pcDao.deleteLastPC());
    }

    LiveData<Integer> getNumOfPCs() {
        return this.numOfPCs;
    }
}
