package com.vat.icare.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.doctor.DoctorChattingActivity;
import com.vat.icare.pojo.User;

import java.util.ArrayList;

public class DoctorRecycler extends RecyclerView.Adapter<DoctorRecycler.viewholder> {

    ArrayList<User> list;
    Context context;

    public DoctorRecycler(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.samplechat_show, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        User useer = list.get(position);
        holder.username.setText(useer.getName());
        if(useer.getImage()!=null){
            byte[] imageAsBytes = Base64.decode(useer.getImage().getBytes(), Base64.DEFAULT);
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + useer.getToken())
                .orderByChild("timestampp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.lastmessage.setText(snapshot1.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorChattingActivity.class);
                intent.putExtra("useridFirebase", useer.getToken());
                intent.putExtra("proficpic", useer.getImage());
                intent.putExtra("userName", useer.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView username, lastmessage;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image_chat);
            username = itemView.findViewById(R.id.usernamee);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }
    }
}
