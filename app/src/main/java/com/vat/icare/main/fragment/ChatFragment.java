package com.vat.icare.main.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.chats.PersonalChats;
import com.vat.icare.databinding.FragmentChatBinding;
import com.vat.icare.databinding.FragmentDoctorChatsBinding;
import com.vat.icare.doctor.adapter.DoctorRecycler;
import com.vat.icare.main.MainActivity;
import com.vat.icare.main.adapter.AdapterChatListUser;
import com.vat.icare.pojo.Doctor;
import com.vat.icare.pojo.User;
import com.vat.icare.pojo.UserHolder;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ArrayList<Doctor> list = new ArrayList<>();
    FirebaseDatabase database;
    AdapterChatListUser adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewChats.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor user = dataSnapshot.getValue(Doctor.class);
                    if (user != null) {
                        String type = user.getType();
                        user.setFirebaseId(dataSnapshot.getKey());
                        if (!user.getFirebaseId().equals(FirebaseAuth.getInstance().getUid())) {
                            if(type!=null){
                                if (type.equals("Doctor")) {
                                    list.add(user);
                                }
                            }
                        }
                    }
                }
                adapter = new AdapterChatListUser(list, getContext());
                binding.recyclerViewChats.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.imgBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}