package com.vat.icare.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vat.icare.R;
import com.vat.icare.doctor.DoctorDetailsActivity;
import com.vat.icare.pojo.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.Holder> {

    Context context;
    ArrayList<Doctor> dataItems;

    public DoctorAdapter(Context context, ArrayList<Doctor> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public DoctorAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_doctor, parent, false);
        return new Holder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.Holder holder,  int position) {
        holder.txtDocName.setText(dataItems.get(position).getName());
        holder.txtDocSpeciality.setText(dataItems.get(position).getSpeciality());
        byte[] imageAsBytes = Base64.decode(dataItems.get(position).getImage().getBytes(), Base64.DEFAULT);
        holder.imgDoct.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        holder.constraintItemDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                intent.putExtra("userId",dataItems.get(position).getFirebaseId());
                intent.putExtra("token",dataItems.get(position).getToken());
                intent.putExtra("image",dataItems.get(position).getImage());
                intent.putExtra("name",dataItems.get(position).getName());
                intent.putExtra("about",dataItems.get(position).getAbout());
                intent.putExtra("speciality",dataItems.get(position).getSpeciality());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtDocName;
        TextView txtDocSpeciality;
        ConstraintLayout constraintItemDoc;
        ImageView imgDoct;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txtDocName);
            constraintItemDoc = itemView.findViewById(R.id.constraintItemDoc);
            txtDocSpeciality = itemView.findViewById(R.id.txtDocSpeciality);
            imgDoct = itemView.findViewById(R.id.imgDoct);

        }
    }
}
