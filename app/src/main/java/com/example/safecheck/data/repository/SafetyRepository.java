package com.example.safecheck.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafeCheckDatabase;
import com.example.safecheck.data.SafetyCheck;
import com.example.safecheck.data.dao.SafetyCheckDao;
import com.example.safecheck.data.relations.CheckWithDefects;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SafetyRepository {

    private final SafetyCheckDao safetyCheckDao;
    private final LiveData<List<CheckWithDefects>> allChecksWithDefects;
    private final ExecutorService executorService;

    public SafetyRepository(Application application) {
        SafeCheckDatabase db = SafeCheckDatabase.getDatabase(application);
        safetyCheckDao = db.safetyCheckDao();
        allChecksWithDefects = safetyCheckDao.getAllChecksWithDefects();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<CheckWithDefects>> getAllChecksWithDefects() {
        return allChecksWithDefects;
    }

    public LiveData<CheckWithDefects> getCheckWithDefects(int checkId) {
        return safetyCheckDao.getCheckWithDefects(checkId);
    }

    public LiveData<List<Defect>> getDefectsForCheck(int checkId) {
        return safetyCheckDao.getDefectsForCheck(checkId);
    }

    public void insertSafetyCheckWithDefects(SafetyCheck safetyCheck, List<Defect> defects) {
        executorService.execute(() -> {
            long newCheckId = safetyCheckDao.insertSafetyCheck(safetyCheck);

            for (Defect defect : defects) {
                Defect defectToInsert = new Defect(
                        (int) newCheckId,
                        defect.getDescription(),
                        defect.getSeverity()
                );
                safetyCheckDao.insertDefect(defectToInsert);
            }
        });
    }

    public void deleteSafetyCheck(SafetyCheck safetyCheck) {
        executorService.execute(() -> safetyCheckDao.deleteSafetyCheck(safetyCheck));
    }
    public void deleteSafetyCheckById(int checkId) {
        executorService.execute(() -> safetyCheckDao.deleteSafetyCheckById(checkId));
    }


}