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
    ArrayList<String> userList = new ArrayList<>();
    FirebaseDatabase database;
    DoctorRecycler adapter;
    String currentUserId, userID;

    public FragmentChatsDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorChatsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerview.setLayoutManager(layoutManager);
        database.getReference().child("Chats_v2").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    userID = key.replace("-", "").replace(currentUserId, "");
                    userList.add(userID);
                }
                getChatList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return binding.getRoot();
    }

    private void getChatList(ArrayList<String> userList) {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setFirebaseId(dataSnapshot.getKey());
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).equals(user.getFirebaseId())) {
                            user.setFirebaseId(dataSnapshot.getKey());
                            if (!list.contains(user)) {
                                list.add(user);
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