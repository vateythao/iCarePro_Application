package com.vat.icare.pojo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHolder extends RecyclerView.ViewHolder{

    private final String TAG = "CA/UserHolder";
    private Activity activity;
    private View view;
    private Context context;
    // Will handle user data
    private DatabaseReference userDatabase;
    private ValueEventListener userListener;
    public UserHolder(Activity activity, View view, Context context) {
        super(view);
        this.activity = activity;
        this.view = view;
        this.context = context;
    }

    public View getView() {
        return view;
    }

    public void setHolder(String userid) {

        final TextView userName = view.findViewById(R.id.txtNameUseChat);
        final CircleImageView userImage = view.findViewById(R.id.imgChatPerson);
        if (userDatabase != null & userListener != null) {
            userDatabase.removeEventListener(userListener);
        }

        // Initialize/Upadte user data

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        userListener = new ValueEventListener() {
            Timer timer; // Will be used to avoid flickering online status when changing activity

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    final String name = dataSnapshot.child("name").getValue().toString();
                    final String image = dataSnapshot.child("image").getValue().toString();

                    userName.setText(name);
                    if (!image.equals("default")) {
                        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        userImage.setImageBitmap(decodedByte);
                    }
                    else {
                        userImage.setImageResource(R.drawable.logo);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "userListener exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "userListener failed: " + databaseError.getMessage());
            }
        };
        userDatabase.addValueEventListener(userListener);
    }
}
