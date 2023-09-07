package com.vat.icare.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vat.icare.R;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {


    public DoctorAdapter() {
    }

    @NonNull
    @Override
    public DoctorAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_doctor, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.Holder holder, int position) {
        holder.txtDocName.setText("");
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtDocName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txtDocName);

        }
    }
}
