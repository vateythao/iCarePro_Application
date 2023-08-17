package com.vat.icare.virtualSigns.adapter;

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

    @Override
    public void onBindViewHolder(@NonNull HistryAdapter.Holder holder, int position) {
        String pulse = String.valueOf(items.get(position).getPulse());
        String dia = String.valueOf(items.get(position).getHeart_rate());
        String sugar = String.valueOf(items.get(position).getSugar());
        String sys = String.valueOf(items.get(position).getBloodP_sys());

        holder.txtPulse.setText(pulse);
        holder.txtDIA.setText(dia);
        holder.txtSugar.setText(sugar);
        holder.txtSYS.setText(sys);
        holder.txtDIA.setText(dia);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView txtSYS;
        TextView txtDIA;
        TextView txtPulse;
        TextView txtSugar;
        TextView txtBloodPressureRate;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.txtSYS = itemView.findViewById(R.id.txtSYS);
            this.txtDIA = itemView.findViewById(R.id.txtDIA);
            this.txtPulse = itemView.findViewById(R.id.txtPulse);
            this.txtBloodPressureRate = itemView.findViewById(R.id.txtBloodPressureRate);
            this.txtSugar = itemView.findViewById(R.id.txtSugar);
        }
    }
}
