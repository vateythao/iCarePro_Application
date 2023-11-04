package com.vat.icare.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityEditProfileDoctorBinding;
import com.vat.icare.pojo.Doctor;

import java.util.HashMap;

public class EditProfileDoctor extends AppCompatActivity {

    String base64;

    ActivityEditProfileDoctorBinding binding;
    DatabaseReference databaseReference;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_doctor);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser=currentFirebaseUser.getUid();

        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieve user data
                    Doctor user = dataSnapshot.getValue(Doctor.class);
                    // Use the user data as needed
                    if (user != null) {
                        String username = user.getName();
                        String email = user.getEmail();
                        String about=user.getAbout();
                        base64 = user.getImage();
                        binding.edtProfileNameDoctor.setText(username);
                        binding.edtEmailEditDoct.setText(email);
                        binding.edtAboutDoctor.setText(about);
                        if(base64!=null){
                            byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
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

        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.edtProfileNameDoctor.getText().toString();
                String about=binding.edtAboutDoctor.getText().toString();
                updatedata(name,base64,about);
            }
        });
        binding.imgEditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // ******** code for crop image
                i.putExtra("crop", "true");
                i.putExtra("aspectX", 356);
                i.putExtra("aspectY", 356);
                i.putExtra("outputX", 356);
                i.putExtra("outputY", 356);
                try {
                    i.putExtra("return-data", true);
                    startActivityForResult(
                            Intent.createChooser(i, "Select Picture"), 0);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void updatedata(String userName, String image, String about) {

        HashMap User = new HashMap();
        User.put("name",userName);
        User.put("image",image);
        User.put("about",about);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(currentUser).updateChildren(User).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(EditProfileDoctor.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                finish();

            }else {
                Toast.makeText(EditProfileDoctor.this,"Failed to Update",Toast.LENGTH_SHORT).show();
            }
        });

    }
}