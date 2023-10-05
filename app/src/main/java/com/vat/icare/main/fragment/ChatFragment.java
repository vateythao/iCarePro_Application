package com.vat.icare.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vat.icare.R;
import com.vat.icare.chats.PersonalChats;
import com.vat.icare.databinding.FragmentChatBinding;
import com.vat.icare.pojo.User;
import com.vat.icare.pojo.UserHolder;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    private FirebaseRecyclerAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        binding.recyclerViewChats.setHasFixedSize(true);
        binding.recyclerViewChats.setLayoutManager(linearLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerViewChats.getContext(),
                linearLayoutManager.getOrientation());
        binding.recyclerViewChats.addItemDecoration(mDividerItemDecoration);

        // Initializing Users database

        DatabaseReference usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDatabase.keepSynced(true); // For offline use

        // Initializing adapter

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(usersDatabase.orderByChild("name"), User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            protected void onBindViewHolder(final UserHolder holder, int position, User model) {
                final String userid = getRef(position).getKey();

                holder.setHolder(userid);
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userProfileIntent = new Intent(getContext(), PersonalChats.class);
                        userProfileIntent.putExtra("userid", userid);
                        startActivity(userProfileIntent);
                    }
                });
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

                return new UserHolder(getActivity(), view, getContext());
            }
        };

        binding.recyclerViewChats.setAdapter(adapter);

        return binding.getRoot();
    }
}