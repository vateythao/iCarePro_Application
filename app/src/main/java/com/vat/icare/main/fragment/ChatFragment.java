package com.vat.icare.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vat.icare.databinding.FragmentChatBinding;
import com.vat.icare.main.adapter.ChatsAdapter;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ChatsAdapter chatsAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        chatsAdapter = new ChatsAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewChats.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChats.setAdapter(chatsAdapter);


        return binding.getRoot();
    }
}