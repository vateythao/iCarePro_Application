package com.vat.icare.registration;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityRegistrationBinding;
import com.vat.icare.login.LoginActivity;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.utils.LoaderDialog;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private final String TAG = "CA/LoginActivity";
    String  base64;
    String  gender;
    LoginViewModel loginViewModel;
    ActivityRegistrationBinding binding;
    LoaderDialog loaderDialog;
    Uri uriPhoto;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loaderDialog = new LoaderDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validation()){
                   String fullName=binding.edtFirstName.getText().toString().trim() +" " +
                           ""+ binding.edtLastName.getText().toString().trim();
                   String email=binding.edtEmail.getText().toString().trim();
                   String password=binding.edtPassword.getText().toString().trim();
                   firebaseRegistrations(fullName,email, password, base64);
               }
            }
        });

        binding.rdGSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbMale) {
                    gender = "Male";
                }
                if (checkedId == R.id.rbFemale) {
                    gender = "Female";
                }
            }
        });

        binding.imgAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        binding.edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input=charSequence.toString();
                String password=binding.edtPassword.getText().toString().trim();
                if(!password.isEmpty()){
                    if(input.trim().equals(password)){
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.valid_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.valid_edittext);
                    }else {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.error_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.error_edittext);
                    }
                }
            }

            public void afterTextChanged(Editable s) {}

        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input=charSequence.toString();
                String confirmpass=binding.edtConfirmPassword.getText().toString().trim();
                if(!confirmpass.isEmpty()){
                    if(input.trim().equals(confirmpass)){
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.valid_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.valid_edittext);
                    }else {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.error_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.error_edittext);
                    }
                }
            }
            public void afterTextChanged(Editable s) {}

        });
    }

    public boolean validation(){
        boolean valid=true;
        if(binding.edtFirstName.getText().toString().trim().equals("")){
            valid=false;
            binding.edtFirstName.setError("First Name Required");
        } else if (binding.edtLastName.getText().toString().trim().equals("")){
            valid=false;
            binding.edtLastName.setError("Last Name Required");
        } else if (binding.edtEmail.getText().toString().trim().equals("")){
            valid=false;
            binding.edtEmail.setError("Email Required");
        }else if (binding.edtPassword.getText().toString().trim().equals("")){
            valid=false;
            binding.edtPassword.setError("Password Required");
        }else if (binding.edtConfirmPassword.getText().toString().trim().equals("")){
            valid=false;
            binding.edtConfirmPassword.setError("Confirm Password Required");
        }else if (binding.edtHeight.getText().toString().trim().equals("")){
            valid=false;
            binding.edtHeight.setError("Height Required");
        }else if (binding.edtWeight.getText().toString().trim().equals("")){
            valid=false;
            binding.edtWeight.setError("Weight Required");
        }else if (binding.edtAge.getText().toString().trim().equals("")){
            valid=false;
            binding.edtAge.setError("Age Required");
        }else if (gender.trim().equals("")){
            valid=false;
            Toast.makeText(this, "Gender missing", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    public void firebaseRegistrations(String fullName, String email,String password,
                                      String profileImage) {
        loaderDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (firebaseUser != null) {
                        String userid = firebaseUser.getUid();
                        Map map = new HashMap<>();
                        map.put("token", FirebaseInstanceId.getInstance().getToken());
                        map.put("name", fullName);
                        map.put("email", email);
                        map.put("password", password);
                        map.put("height", binding.edtHeight.getText().toString().trim());
                        map.put("weight", binding.edtWeight.getText().toString().trim());
                        map.put("age", binding.edtAge.getText().toString().trim());
                        map.put("gender", gender);
                        map.put("image", profileImage);
                        map.put("date", ServerValue.TIMESTAMP);

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).setValue(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                    String firebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseAuth.getInstance().signOut();
                                    loaderDialog.dismiss();
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(RegistrationActivity.this);
                                    builder2.setTitle("Registration completed!");
                                    builder2.setMessage("we have sent you E-mail on " + email + " for verification");
                                    builder2.setCancelable(false);
                                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    dialog.dismiss();
                                                    Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    builder2.show();

                                }
                                else {
                                    Log.d(TAG, "registerData failed: " + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "failed: " + task.getException().getMessage());
                    loaderDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] byteArray = stream.toByteArray();
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 99, bao);
                byte[] ba = bao.toByteArray();

                String convbase64 = Base64.encodeToString(ba, Base64.DEFAULT);

                // uncommend this and replace with profile Picture Id id
                base64 = convbase64.replaceAll("\n","");
                if (base64 != null) {
                    binding.imgProfile.setImageBitmap(bitmap);
                    binding.imgAddPhoto.setVisibility(View.GONE);
                } else {
                    binding.imgAddPhoto.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}