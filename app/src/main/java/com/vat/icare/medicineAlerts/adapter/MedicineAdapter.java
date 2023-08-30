package com.vat.icare.medicineAlerts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vat.icare.R;
import com.vat.icare.medicineAlerts.Model;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.Myviewholder>{
    ArrayList<Model> dataHolder = new ArrayList<Model>();

    public MedicineAdapter(ArrayList<Model> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.mTitle.setText(dataHolder.get(position).getTitle());
        holder.mDate.setText(dataHolder.get(position).getDate());
        holder.mTime.setText(dataHolder.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }


    class Myviewholder extends RecyclerView.ViewHolder {

        TextView mTitle, mDate, mTime;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }
}
