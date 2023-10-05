package com.vat.icare.calls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityIncomingCallBinding;
import com.vat.icare.main.MainActivity;

import java.util.HashMap;

public class InComingCallActivity extends AppCompatActivity {
    ActivityIncomingCallBinding binding;
    Context context;
    private String rec_userid = "";
    private String calledUsername = "";
    private String calledProfilePic = "";
    private String caller_userid = "", caller_profile_pic = "", caller_username = "", detect_touch = "", outgoingId = "", incomingID = "";
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call);
        context = this;
        caller_userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rec_userid = getIntent().getExtras().get("visiter_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        binding.declineCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detect_touch = "Touched";
                cancelCall();
            }
        });

        binding.makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HashMap<String, Object> callPickMap = new HashMap<>();
                callPickMap.put("picked", "picked");

                userRef.child(caller_userid).child("Incoming").updateChildren(callPickMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, VideoCallActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        setProfileInfo();
    }

    private void setProfileInfo() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef();
                if (snapshot.child(rec_userid).exists()) {
                    calledProfilePic = snapshot.child(rec_userid).child("image").getValue().toString();
                    calledUsername = snapshot.child(rec_userid).child("name").getValue().toString();

                    binding.calledUsername.setText(calledUsername);
                    Picasso.get().load(calledProfilePic).into(binding.calledProfilePic);
                }
                if (snapshot.child(caller_userid).exists()) {
                    calledProfilePic = snapshot.child(caller_userid).child("image").getValue().toString();
                    calledUsername = snapshot.child(caller_userid).child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelCall() {
        //Caller_Side//
        userRef.child(caller_userid).child("Outgoing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("HasCalled")) {
                    outgoingId = snapshot.child("HasCalled").getValue().toString();

                    userRef.child(outgoingId).child("Incoming").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                userRef.child(caller_userid).child("Outgoing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(context, MainActivity.class));
                                        finish();
                                    }
                                });
                            }

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Reciever Side//
        userRef.child(caller_userid).child("Incoming").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("HasCalled")) {
                    incomingID = snapshot.child("IsCalling").getValue().toString();

                    userRef.child(incomingID).child("Outgoing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                userRef.child(caller_userid).child("Incoming").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        startActivity(new Intent(context, VideoCallActivity.class));
                                        finish();

                                    }
                                });
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}