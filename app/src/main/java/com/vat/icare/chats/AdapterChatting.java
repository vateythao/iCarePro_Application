package com.vat.icare.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vat.icare.R;

public class AdapterChatting extends RecyclerView.Adapter<AdapterChatting.Holder> {
    Context context;

    public AdapterChatting(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterChatting.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
       return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatting.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }
    class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
