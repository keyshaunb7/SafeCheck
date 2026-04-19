package com.example.safecheck.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafetyCheck;
import com.example.safecheck.data.relations.CheckWithDefects;
import com.example.safecheck.data.repository.SafetyRepository;

import java.util.List;

public class SafetyCheckViewModel extends AndroidViewModel {

    private final SafetyRepository repository;
    private final LiveData<List<CheckWithDefects>> allChecksWithDefects;

    public SafetyCheckViewModel(@NonNull Application application) {
        super(application);
        repository = new SafetyRepository(application);
        allChecksWithDefects = repository.getAllChecksWithDefects();
    }

    public LiveData<List<CheckWithDefects>> getAllChecksWithDefects() {
        return allChecksWithDefects;
    }

    public LiveData<CheckWithDefects> getCheckWithDefects(int checkId) {
        return repository.getCheckWithDefects(checkId);
    }

    public void insertSafetyCheckWithDefects(SafetyCheck safetyCheck, List<Defect> defects) {
        repository.insertSafetyCheckWithDefects(safetyCheck, defects);
    }

    public void deleteSafetyCheck(SafetyCheck safetyCheck) {
        repository.deleteSafetyCheck(safetyCheck);
    }
    public void deleteSafetyCheckById(int checkId) {
        repository.deleteSafetyCheckById(checkId);
    }
}