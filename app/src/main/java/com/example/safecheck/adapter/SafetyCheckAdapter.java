package com.example.safecheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safecheck.R;
import com.example.safecheck.data.relations.CheckWithDefects;

import java.util.ArrayList;
import java.util.List;

public class SafetyCheckAdapter extends RecyclerView.Adapter<SafetyCheckAdapter.SafetyCheckViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CheckWithDefects checkWithDefects);
    }

    private List<CheckWithDefects> checks = new ArrayList<>();
    private final OnItemClickListener listener;

    public SafetyCheckAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setChecks(List<CheckWithDefects> checks) {
        this.checks = (checks == null) ? new ArrayList<>() : checks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SafetyCheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_safety_check, parent, false);
        return new SafetyCheckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SafetyCheckViewHolder holder, int position) {
        CheckWithDefects current = checks.get(position);

        holder.txtDate.setText(current.safetyCheck.getDate());
        holder.txtVehicleReg.setText(current.safetyCheck.getVehicleRegistration());

        int defectCount = (current.defects == null) ? 0 : current.defects.size();
        holder.txtDefectCount.setText(defectCount + " Defect" + (defectCount == 1 ? "" : "s"));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(current));
    }

    @Override
    public int getItemCount() {
        return checks.size();
    }

    static class SafetyCheckViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtVehicleReg, txtDefectCount;

        public SafetyCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtVehicleReg = itemView.findViewById(R.id.txtVehicleReg);
            txtDefectCount = itemView.findViewById(R.id.txtDefectCount);
        }
    }
}