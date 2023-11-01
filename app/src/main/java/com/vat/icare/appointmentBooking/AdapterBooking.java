package com.vat.icare.appointmentBooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vat.icare.R;
import com.vat.icare.pojo.Booking;

import java.util.List;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.Holder> {

    Context context;
    private List<Booking> bookingsList;

    public AdapterBooking(Context context, List<Booking> messagesList) {
        this.context = context;
        this.bookingsList = messagesList;
    }

    @NonNull
    @Override
    public AdapterBooking.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.Holder holder, int position) {
        holder.txtNamePatent.setText("Patient Name: "+ bookingsList.get(position).getPatientName());
        holder.txtBookingDate.setText("Date Booking: "+bookingsList.get(position).getDate());
        holder.txtBookingTime.setText("Time Booking: "+bookingsList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtNamePatent;
        TextView txtBookingDate;
        TextView txtBookingTime;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtNamePatent = itemView.findViewById(R.id.txtNamePatent);
            txtBookingDate = itemView.findViewById(R.id.txtBookingDate);
            txtBookingTime = itemView.findViewById(R.id.txtBookingTime);
        }
    }
}
