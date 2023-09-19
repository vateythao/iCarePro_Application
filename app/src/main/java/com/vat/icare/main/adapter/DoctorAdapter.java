package com.vat.icare.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vat.icare.R;
import com.vat.icare.doctor.DoctorDetailsActivity;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {

    Context context;

    public DoctorAdapter(Context context) {
        this.context = context;
    }

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
        holder.constraintItemDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtDocName;
        ConstraintLayout constraintItemDoc;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txtDocName);
            constraintItemDoc = itemView.findViewById(R.id.constraintItemDoc);

        }
    }
}
