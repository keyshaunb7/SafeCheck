package com.example.safecheck;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.safecheck.data.Defect;
import com.example.safecheck.data.SafetyCheck;
import com.example.safecheck.viewmodel.AddSafetyCheckViewModel;
import com.example.safecheck.viewmodel.SafetyCheckViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddSafetyCheckActivity extends AppCompatActivity {

    private SafetyCheckViewModel safetyCheckViewModel;
    private AddSafetyCheckViewModel formViewModel;

    private EditText edtDate;
    private EditText edtVehicleReg;
    private EditText edtDriverName;
    private EditText edtDefect1Description;
    private EditText edtDefect2Description;
    private RadioGroup radioGroupStatus;
    private Spinner spinnerDefect1Severity;
    private Spinner spinnerDefect2Severity;
    private Button btnSaveCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_safety_check);

        safetyCheckViewModel = new ViewModelProvider(this).get(SafetyCheckViewModel.class);
        formViewModel = new ViewModelProvider(this).get(AddSafetyCheckViewModel.class);

        edtDate = findViewById(R.id.edtDate);
        edtVehicleReg = findViewById(R.id.edtVehicleReg);
        edtDriverName = findViewById(R.id.edtDriverName);
        edtDefect1Description = findViewById(R.id.edtDefect1Description);
        edtDefect2Description = findViewById(R.id.edtDefect2Description);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
        spinnerDefect1Severity = findViewById(R.id.spinnerDefect1Severity);
        spinnerDefect2Severity = findViewById(R.id.spinnerDefect2Severity);
        btnSaveCheck = findViewById(R.id.btnSaveCheck);

        setupSeveritySpinner(spinnerDefect1Severity);
        setupSeveritySpinner(spinnerDefect2Severity);
        setupDatePicker();
        restoreFormState();
        setupFormStateSaving();

        btnSaveCheck.setOnClickListener(v -> saveSafetyCheck());
    }

    private void setupSeveritySpinner(Spinner spinner) {
        String[] severityOptions = {"Select Severity", "Low", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                severityOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupDatePicker() {
        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddSafetyCheckActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format(
                                Locale.UK,
                                "%04d-%02d-%02d",
                                selectedYear,
                                selectedMonth + 1,
                                selectedDay
                        );
                        edtDate.setText(formattedDate);
                        formViewModel.date = formattedDate;
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.show();
        });
    }

    private void restoreFormState() {
        edtDate.setText(formViewModel.date);
        edtVehicleReg.setText(formViewModel.vehicleReg);
        edtDriverName.setText(formViewModel.driverName);
        edtDefect1Description.setText(formViewModel.defect1Description);
        edtDefect2Description.setText(formViewModel.defect2Description);

        if ("Pass".equals(formViewModel.overallStatus)) {
            radioGroupStatus.check(R.id.radioPass);
        } else if ("Fail".equals(formViewModel.overallStatus)) {
            radioGroupStatus.check(R.id.radioFail);
        }

        setSpinnerSelection(spinnerDefect1Severity, formViewModel.defect1Severity);
        setSpinnerSelection(spinnerDefect2Severity, formViewModel.defect2Severity);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<?> adapter = (ArrayAdapter<?>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setupFormStateSaving() {
        radioGroupStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPass) {
                formViewModel.overallStatus = "Pass";
            } else if (checkedId == R.id.radioFail) {
                formViewModel.overallStatus = "Fail";
            }
        });
    }

    private void saveCurrentFormToViewModel() {
        formViewModel.date = edtDate.getText().toString().trim();
        formViewModel.vehicleReg = edtVehicleReg.getText().toString().trim();
        formViewModel.driverName = edtDriverName.getText().toString().trim();
        formViewModel.defect1Description = edtDefect1Description.getText().toString().trim();
        formViewModel.defect2Description = edtDefect2Description.getText().toString().trim();
        formViewModel.defect1Severity = spinnerDefect1Severity.getSelectedItem().toString();
        formViewModel.defect2Severity = spinnerDefect2Severity.getSelectedItem().toString();
    }

    private void saveSafetyCheck() {
        saveCurrentFormToViewModel();

        String date = formViewModel.date;
        String vehicleReg = formViewModel.vehicleReg;
        String driverName = formViewModel.driverName;
        String overallStatus = formViewModel.overallStatus;

        if (TextUtils.isEmpty(vehicleReg)) {
            Toast.makeText(this, "Please enter vehicle details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please select the date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(driverName)) {
            Toast.makeText(this, "Please enter driver name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(overallStatus)) {
            Toast.makeText(this, "Please select overall status", Toast.LENGTH_SHORT).show();
            return;
        }

        SafetyCheck safetyCheck = new SafetyCheck(date, vehicleReg, driverName, overallStatus);

        List<Defect> defects = new ArrayList<>();

        if (!TextUtils.isEmpty(formViewModel.defect1Description)
                && !"Select Severity".equals(formViewModel.defect1Severity)) {
            defects.add(new Defect(0, formViewModel.defect1Description, formViewModel.defect1Severity));
        }

        if (!TextUtils.isEmpty(formViewModel.defect2Description)
                && !"Select Severity".equals(formViewModel.defect2Severity)) {
            defects.add(new Defect(0, formViewModel.defect2Description, formViewModel.defect2Severity));
        }

        safetyCheckViewModel.insertSafetyCheckWithDefects(safetyCheck, defects);

        formViewModel.date = "";
        formViewModel.vehicleReg = "";
        formViewModel.driverName = "";
        formViewModel.overallStatus = "";
        formViewModel.defect1Description = "";
        formViewModel.defect1Severity = "Select Severity";
        formViewModel.defect2Description = "";
        formViewModel.defect2Severity = "Select Severity";

        Toast.makeText(this, "Safety check saved", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentFormToViewModel();
    }
}