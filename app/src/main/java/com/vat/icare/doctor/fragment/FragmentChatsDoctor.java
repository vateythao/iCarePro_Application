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
import com.vat.icare.databinding.FragmentDoctorChatsBinding;
import com.vat.icare.doctor.adapter.DoctorRecycler;
import com.vat.icare.pojo.User;

import java.util.ArrayList;

public class FragmentChatsDoctor extends Fragment {

    FragmentDoctorChatsBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;
    DoctorRecycler adapter;

    public FragmentChatsDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();

        binding = FragmentDoctorChatsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                                if (type.equals("type")) {
                                    list.add(user);
                                }
                            }
                        }
                    }
                }
                adapter = new DoctorRecycler(list, getContext());
                binding.chatRecyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}