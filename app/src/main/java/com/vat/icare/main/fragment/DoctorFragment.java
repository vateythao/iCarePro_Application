package com.vat.icare.main.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.chats.PersonalChats;
import com.vat.icare.databinding.FragmentDoctorBinding;
import com.vat.icare.main.adapter.DoctorAdapter;
import com.vat.icare.pojo.Doctor;
import com.vat.icare.pojo.DoctorHolder;

import java.util.ArrayList;
import java.util.List;

public class DoctorFragment extends Fragment {

    FragmentDoctorBinding binding;
    DoctorAdapter doctorAdapter;
    ArrayList<Doctor> dataItems= new ArrayList<>();
    public DoctorFragment() {
        // Required empty public constructor
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorBinding.inflate(inflater, container, false);
        binding.getViewModel();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerViewDoctor.setLayoutManager(layoutManager);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://icarepro-7a66c-default-rtdb.firebaseio.com/Doctors");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                dataItems.clear();
                for(DataSnapshot dataSnapshotTwo : dataSnapshot.getChildren()){
                    Doctor doctors = dataSnapshotTwo.getValue(Doctor.class);
                    dataItems.add(doctors);
                }
                doctorAdapter=new DoctorAdapter(getContext(),dataItems);
                binding.recyclerViewDoctor.setAdapter(doctorAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}