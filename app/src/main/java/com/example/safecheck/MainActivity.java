package com.example.safecheck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safecheck.adapter.SafetyCheckAdapter;
import com.example.safecheck.viewmodel.SafetyCheckViewModel;

public class MainActivity extends AppCompatActivity {

    private SafetyCheckViewModel viewModel;
    private SafetyCheckAdapter adapter;
    private RecyclerView recyclerChecks;
    private Button btnAddCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerChecks = findViewById(R.id.recyclerChecks);
        btnAddCheck = findViewById(R.id.btnAddCheck);

        recyclerChecks.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SafetyCheckAdapter(checkWithDefects -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("checkId", checkWithDefects.safetyCheck.getCheckId());
            startActivity(intent);
        });

        recyclerChecks.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SafetyCheckViewModel.class);

        viewModel.getAllChecksWithDefects().observe(this, checks -> {
            if (checks != null) {
                adapter.setChecks(checks);
            }
        });

        btnAddCheck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddSafetyCheckActivity.class);
            startActivity(intent);
        });
    }
}