package com.vat.icare.doctor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityDoctorMainBinding;
import com.vat.icare.doctor.adapter.DoctorRecycler;
import com.vat.icare.pojo.User;

import java.util.ArrayList;

public class DoctorMainActivity extends AppCompatActivity {
    ActivityDoctorMainBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;
    DoctorRecycler adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerview.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String type = user.getType();
                        user.setFirebaseId(dataSnapshot.getKey());
                        if (!user.getFirebaseId().equals(FirebaseAuth.getInstance().getUid())) {
                          if(type!=null){
                              if (type.equals("User")) {
                                  list.add(user);
                              }
                          }
                        }
                    }
                }
                adapter = new DoctorRecycler(list, binding.getRoot().getContext());
                binding.chatRecyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}