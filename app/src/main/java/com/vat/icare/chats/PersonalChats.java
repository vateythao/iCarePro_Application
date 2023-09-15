package com.vat.icare.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityPersonnalChatsBinding;

public class PersonalChats extends AppCompatActivity {
    ActivityPersonnalChatsBinding binding;
    AdapterChatting adapterChatting;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personnal_chats);
        context=this;
        adapterChatting=new AdapterChatting(context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerPersonal.setLayoutManager(linearLayoutManager);
        binding.recyclerPersonal.setAdapter(adapterChatting);
    }
}