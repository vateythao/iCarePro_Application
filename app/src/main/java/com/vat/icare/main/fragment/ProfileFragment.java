package com.vat.icare.main.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.chats.fcm.SessionManager;
import com.vat.icare.databinding.FragmentChatBinding;
import com.vat.icare.databinding.FragmentProfileBinding;
import com.vat.icare.editProfile.EditProfileActivity;
import com.vat.icare.login.LoginActivity;
import com.vat.icare.main.MainActivity;
import com.vat.icare.pojo.User;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    private Context mContext;
    private MainActivity mainActivity;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mainActivity = (MainActivity) context;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogout.setOnClickListener(view -> {

            new AlertDialog.Builder(mContext)
                    .setTitle("Logout")
                    .setPositiveButton("OK", (dialog, which) -> {
                        final FirebaseAuth mAuth;
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        SessionManager sessionManager = new SessionManager(mContext);
                        sessionManager.clearAll();
                        showClearTopScreen(LoginActivity.class);
                    })
                    .setNegativeButton("Cancel", null)
                    .setMessage( "Are you sure, want to logout?"  )
                    .show();



        });

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve user data
                    User user = dataSnapshot.getValue(User.class);

                    // Use the user data as needed
                    if (user != null) {
                        String username = user.getName();
                        String email = user.getEmail();
                        String imgUser = user.getImage();
                        binding.txtUserName.setText(username);
                        binding.txtEmailId.setText(email);
                        if(!imgUser.equals("")){
                            byte[] imageAsBytes = Base64.decode(imgUser.getBytes(), Base64.DEFAULT);
                            binding.imgUser.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
        return binding.getRoot();
    }

    public void showClearTopScreen(final Class<?> cls) {
        final Intent intent = new Intent(mContext, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        mainActivity.finish();
    }
}