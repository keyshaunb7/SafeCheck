package com.example.safecheck.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.safecheck.data.dao.SafetyCheckDao;

@Database(entities = {SafetyCheck.class, Defect.class}, version = 1, exportSchema = false)
public abstract class SafeCheckDatabase extends RoomDatabase {

    public abstract SafetyCheckDao safetyCheckDao();

    private static volatile SafeCheckDatabase INSTANCE;

    public static SafeCheckDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SafeCheckDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    SafeCheckDatabase.class,
                                    "safecheck_database"
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}