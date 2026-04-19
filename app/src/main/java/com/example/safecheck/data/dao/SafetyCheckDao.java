package com.example.safecheck.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafetyCheck;
import com.example.safecheck.data.relations.CheckWithDefects;

import java.util.List;

@Dao
public interface SafetyCheckDao {

    @Insert
    long insertSafetyCheck(SafetyCheck safetyCheck);

    @Insert
    void insertDefect(Defect defect);

    @Insert
    void insertDefects(List<Defect> defects);

    @Transaction
    @Query("SELECT * FROM safety_checks WHERE checkId = :checkId")
    LiveData<CheckWithDefects> getCheckWithDefects(int checkId);

    @Transaction
    @Query("SELECT * FROM safety_checks ORDER BY checkId DESC")
    LiveData<List<CheckWithDefects>> getAllChecksWithDefects();

    @Query("SELECT * FROM safety_checks ORDER BY checkId DESC")
    LiveData<List<SafetyCheck>> getAllSafetyChecks();

    @Query("DELETE FROM safety_checks WHERE checkId = :checkId")
    void deleteSafetyCheckById(int checkId);

    @Delete
    void deleteSafetyCheck(SafetyCheck safetyCheck);
}