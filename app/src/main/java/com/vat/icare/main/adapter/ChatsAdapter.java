package com.vat.icare.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.vat.icare.R;
import com.vat.icare.chats.PersonalChats;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.Holder> {

    Context context;

    public ChatsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.Holder holder, int position) {
        holder.cardChatClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PersonalChats.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }
    class Holder extends RecyclerView.ViewHolder{
        CardView cardChatClicked;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cardChatClicked=itemView.findViewById(R.id.cardChatClicked);
        }
    }
}
