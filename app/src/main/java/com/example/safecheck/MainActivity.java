package com.example.safecheck;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafetyCheck;
import com.example.safecheck.data.relations.CheckWithDefects;
import com.example.safecheck.viewmodel.SafetyCheckViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SafetyCheckViewModel viewModel;
    private TextView txtOutput;
    private Button btnInsertTest;
    private Button btnLoadData;
    private Button btnDeleteLatest;

    private List<CheckWithDefects> currentChecks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);
        btnInsertTest = findViewById(R.id.btnInsertTest);
        btnLoadData = findViewById(R.id.btnLoadData);
        btnDeleteLatest = findViewById(R.id.btnDeleteLatest);

        viewModel = new ViewModelProvider(this).get(SafetyCheckViewModel.class);

        viewModel.getAllChecksWithDefects().observe(this, checks -> {
            if (checks == null || checks.isEmpty()) {
                currentChecks = new ArrayList<>();
                txtOutput.setText("No checks found.");
                return;
            }

            currentChecks = checks;

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < checks.size(); i++) {
                CheckWithDefects check = checks.get(i);

                builder.append("Check ").append(i + 1).append("\n");
                builder.append("ID: ").append(check.safetyCheck.getCheckId()).append("\n");
                builder.append("Date: ").append(check.safetyCheck.getDate()).append("\n");
                builder.append("Vehicle: ").append(check.safetyCheck.getVehicleRegistration()).append("\n");
                builder.append("Driver: ").append(check.safetyCheck.getDriverName()).append("\n");
                builder.append("Status: ").append(check.safetyCheck.getOverallStatus()).append("\n");
                builder.append("Defects:\n");

                if (check.defects != null && !check.defects.isEmpty()) {
                    for (Defect defect : check.defects) {
                        builder.append("- ")
                                .append(defect.getDescription())
                                .append(" (")
                                .append(defect.getSeverity())
                                .append(")\n");
                    }
                } else {
                    builder.append("None\n");
                }

                builder.append("\n--------------------\n\n");
            }

            txtOutput.setText(builder.toString());
        });

        btnInsertTest.setOnClickListener(v -> {
            SafetyCheck safetyCheck = new SafetyCheck(
                    "2026-04-19",
                    "AB12 CDE",
                    "Keyshaun",
                    "Fail"
            );

            List<Defect> defects = new ArrayList<>();
            defects.add(new Defect(0, "Cracked Mirror", "High"));
            defects.add(new Defect(0, "Low Tyre Pressure", "Low"));

            viewModel.insertSafetyCheckWithDefects(safetyCheck, defects);
        });

        btnLoadData.setOnClickListener(v -> {
            if (currentChecks == null || currentChecks.isEmpty()) {
                txtOutput.setText("No checks found.");
            }
        });

        btnDeleteLatest.setOnClickListener(v -> {
            if (currentChecks == null || currentChecks.isEmpty()) {
                txtOutput.setText("No checks available to delete.");
                return;
            }

            int latestCheckId = currentChecks.get(0).safetyCheck.getCheckId();
            viewModel.deleteSafetyCheckById(latestCheckId);
        });
    }
}