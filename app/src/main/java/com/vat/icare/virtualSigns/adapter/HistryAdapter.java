package com.vat.icare.virtualSigns.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.vat.icare.R;
import com.vat.icare.utils.romDB.VitalSign;

import java.util.ArrayList;

public class HistryAdapter extends RecyclerView.Adapter<HistryAdapter.Holder> {

    ArrayList<VitalSign> items;

    public HistryAdapter(ArrayList<VitalSign> students) {
        this.items = students;
    }

    @NonNull
    @Override
    public HistryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.itemhistory, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistryAdapter.Holder holder, int position) {
        String pulse = String.valueOf(items.get(position).getPulse());
        String dia = String.valueOf(items.get(position).getBloodP_dia());
        String sugar = String.valueOf(items.get(position).getSugar());
        String sys = String.valueOf(items.get(position).getBloodP_sys());

        holder.BP2V.setText(sys+"/"+dia);
        holder.HRV.setText(dia);
        holder.RRV.setText(pulse);
        holder.txtSugar.setText(sugar);

        holder.txtDate.setText("Date: "+items.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView BP2V;
        TextView HRV;
        TextView RRV;
        TextView txtSugar;
        TextView txtDate;


        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.BP2V = itemView.findViewById(R.id.BP2V);
            this.HRV = itemView.findViewById(R.id.HRV);
            this.RRV = itemView.findViewById(R.id.RRV);
            this.txtSugar = itemView.findViewById(R.id.txtSugar);
            this.txtDate = itemView.findViewById(R.id.txtDate);

        }
    }
}
