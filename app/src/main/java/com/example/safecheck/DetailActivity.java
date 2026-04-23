package com.example.safecheck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.relations.CheckWithDefects;
import com.example.safecheck.viewmodel.SafetyCheckViewModel;

public class DetailActivity extends AppCompatActivity {

    private SafetyCheckViewModel viewModel;

    private TextView txtDetailDate;
    private TextView txtDetailVehicle;
    private TextView txtDetailDriver;
    private TextView txtDetailStatus;
    private TextView txtDefectsList;
    private Button btnEmailReport;
    private Button btnDeleteCheck;

    private CheckWithDefects currentCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtDetailDate = findViewById(R.id.txtDetailDate);
        txtDetailVehicle = findViewById(R.id.txtDetailVehicle);
        txtDetailDriver = findViewById(R.id.txtDetailDriver);
        txtDetailStatus = findViewById(R.id.txtDetailStatus);
        txtDefectsList = findViewById(R.id.txtDefectsList);
        btnEmailReport = findViewById(R.id.btnEmailReport);
        btnDeleteCheck = findViewById(R.id.btnDeleteCheck);

        int checkId = getIntent().getIntExtra("checkId", -1);

        if (checkId == -1) {
            Toast.makeText(this, "Invalid safety check.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(SafetyCheckViewModel.class);

        viewModel.getCheckWithDefects(checkId).observe(this, checkWithDefects -> {
            if (checkWithDefects == null || checkWithDefects.safetyCheck == null) {
                Toast.makeText(this, "Safety check not found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            currentCheck = checkWithDefects;

            txtDetailDate.setText("Date: " + checkWithDefects.safetyCheck.getDate());
            txtDetailVehicle.setText("Vehicle: " + checkWithDefects.safetyCheck.getVehicleRegistration());
            txtDetailDriver.setText("Driver: " + checkWithDefects.safetyCheck.getDriverName());
            txtDetailStatus.setText("Status: " + checkWithDefects.safetyCheck.getOverallStatus());
            txtDefectsList.setText(buildDefectsText(checkWithDefects));
        });

        btnEmailReport.setOnClickListener(v -> sendEmailReport());

        btnDeleteCheck.setOnClickListener(v -> {
            if (currentCheck == null || currentCheck.safetyCheck == null) {
                Toast.makeText(this, "No check available to delete.", Toast.LENGTH_SHORT).show();
                return;
            }

            int currentCheckId = currentCheck.safetyCheck.getCheckId();
            viewModel.deleteSafetyCheckById(currentCheckId);

            Toast.makeText(this, "Safety check deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String buildDefectsText(CheckWithDefects checkWithDefects) {
        StringBuilder defectsText = new StringBuilder();

        if (checkWithDefects.defects != null && !checkWithDefects.defects.isEmpty()) {
            for (Defect defect : checkWithDefects.defects) {
                defectsText.append("- ")
                        .append(defect.getDescription())
                        .append(" (")
                        .append(defect.getSeverity())
                        .append(")\n");
            }
        } else {
            defectsText.append("No defects recorded.");
        }

        return defectsText.toString();
    }

    private void sendEmailReport() {
        if (currentCheck == null || currentCheck.safetyCheck == null) {
            Toast.makeText(this, "No report data available.", Toast.LENGTH_SHORT).show();
            return;
        }

        String vehicleReg = currentCheck.safetyCheck.getVehicleRegistration();
        String subject = "Safety Defect Report: " + vehicleReg;

        StringBuilder body = new StringBuilder();
        body.append("Vehicle: ").append(vehicleReg).append("\n");
        body.append("Driver: ").append(currentCheck.safetyCheck.getDriverName()).append("\n");
        body.append("Date: ").append(currentCheck.safetyCheck.getDate()).append("\n");
        body.append("Status: ").append(currentCheck.safetyCheck.getOverallStatus()).append("\n\n");
        body.append("Defects:\n");
        body.append(buildDefectsText(currentCheck));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }
}