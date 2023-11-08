package com.vat.icare.doctor.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.appointmentBooking.AdapterBooking;
import com.vat.icare.databinding.FragmentBookingBinding;
import com.vat.icare.main.adapter.AdapterChatListUser;
import com.vat.icare.main.adapter.DoctorAdapter;
import com.vat.icare.pojo.Booking;
import com.vat.icare.pojo.Doctor;

import java.util.ArrayList;
import java.util.Objects;

public class BookingFragment extends Fragment {
    FragmentBookingBinding binding;
    private String strSender, strReceiver,currentUserId;
    ArrayList<Booking> list = new ArrayList<>();
    FirebaseDatabase database;
    AdapterBooking adapter;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewBooking.setLayoutManager(layoutManager);

        database.getReference().child("Bookings").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking doctors = dataSnapshot.getValue(Booking.class);
                    if (doctors != null) {
                        if (currentUserId.equals(doctors.getDoctorId())) {
                            list.add(doctors);
                        }
                    }
                }
                if (list.isEmpty()) {
                    binding.txtNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    binding.txtNoDataFound.setVisibility(View.GONE);
                }
                adapter = new AdapterBooking(getContext(), list);
                binding.recyclerViewBooking.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return binding.getRoot();
    }
}