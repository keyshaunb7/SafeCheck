package com.example.safecheck.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafetyCheck;

import java.util.List;

public class CheckWithDefects {

    @Embedded
    public SafetyCheck safetyCheck;

    @Relation(
            parentColumn = "checkId",
            entityColumn = "parentCheckId"
    )
    public List<Defect> defects;
}