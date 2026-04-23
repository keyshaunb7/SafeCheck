package com.example.safecheck.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "defects",
        foreignKeys = @ForeignKey(
                entity = SafetyCheck.class,
                parentColumns = "checkId",
                childColumns = "parentCheckId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("parentCheckId")}
)
public class Defect {

    @PrimaryKey(autoGenerate = true)
    private int defectId;

    private int parentCheckId;

    @NonNull
    private String description;

    @NonNull
    private String severity;

    public Defect() {}

    public Defect(int parentCheckId, @NonNull String description, @NonNull String severity) {
        this.parentCheckId = parentCheckId;
        this.description = description;
        this.severity = severity;
    }

    public int getDefectId() {
        return defectId;
    }

    public void setDefectId(int defectId) {
        this.defectId = defectId;
    }

    public int getParentCheckId() {
        return parentCheckId;
    }

    public void setParentCheckId(int parentCheckId) {
        this.parentCheckId = parentCheckId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(@NonNull String severity) {
        this.severity = severity;
    }
}