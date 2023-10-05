package com.vat.icare.doctorRegistration;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.vat.icare.databinding.ActivityDoctorRegistrationBinding;
import com.vat.icare.login.LoginActivity;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.utils.LoaderDialog;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DoctorRegistration extends AppCompatActivity {
    ActivityDoctorRegistrationBinding binding;
    private final String TAG = "CA/DoctorRegistration";
    String base64;
    String gender = "";
    LoginViewModel loginViewModel;
    LoaderDialog loaderDialog;
    Context context;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String speciality;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_registration);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loaderDialog = new LoaderDialog(this);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    String certificationNo = binding.edtCertification.getText().toString().trim();
                    String fullName = binding.edtFirstName.getText().toString().trim();
                    String email = binding.edtEmail.getText().toString().trim();
                    String password = binding.edtPassword.getText().toString().trim();
                    String medicalDegree = binding.edtMedicalDegree.getText().toString().trim();
                    String collage = binding.edtCollage.getText().toString();
                    if (base64.isEmpty()) {
                        Toast.makeText(context, "Profile Image Missing", Toast.LENGTH_SHORT).show();
                    } else if (speciality.toString().equals("")) {
                        Toast.makeText(context, "speciality Missing", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseRegistrations(certificationNo, fullName, email, password, medicalDegree, speciality, collage, base64);
                    }
                }
            }
        });
        binding.rdGSelectionDoc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbMaleDoc) {
                    gender = "Male";
                }
                if (checkedId == R.id.rbFemaleDoc) {
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
                String input = charSequence.toString();
                String password = binding.edtPassword.getText().toString().trim();
                if (!password.isEmpty()) {
                    if (input.trim().equals(password)) {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.valid_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.valid_edittext);
                    } else {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.error_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.error_edittext);
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }

        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                String confirmpass = binding.edtConfirmPassword.getText().toString().trim();
                if (!confirmpass.isEmpty()) {
                    if (input.trim().equals(confirmpass)) {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.valid_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.valid_edittext);
                    } else {
                        binding.llConfirmPassword.setBackgroundResource(R.drawable.error_edittext);
                        binding.llPassword.setBackgroundResource(R.drawable.error_edittext);
                    }
                }
            }

            public void afterTextChanged(Editable s) {
            }

        });
        binding.spinnerSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                speciality = binding.spinnerSpeciality.getSelectedItem().toString();
                if (speciality.equals("Others")) {
                    binding.llOtherSpeciality.setVisibility(View.VISIBLE);
                } else {
                    binding.llOtherSpeciality.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public boolean validation() {
        boolean valid = true;
        if (binding.edtFirstName.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtFirstName.setError("First Name Required");
        }
        if (binding.edtCertification.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtCertification.setError("Certification no Is required");
        }
        if (binding.edtEmail.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtEmail.setError("Email Required");
        }
        if (binding.edtPassword.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtPassword.setError("Password Required");
        }
        if (binding.edtConfirmPassword.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtConfirmPassword.setError("Confirm Password Required");
        }
        if (binding.edtMedicalDegree.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtMedicalDegree.setError("Medical Degree is required");
        }
        if (binding.edtCollage.getText().toString().trim().equals("")) {
            valid = false;
            binding.edtCollage.setError("University/Collage is required");
        }
        if (gender.trim().equals("")) {
            valid = false;
            Toast.makeText(this, "Gender missing", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    public void firebaseRegistrations(String certification, String nameFull, String email, String password, String medicalDegree,
                                      String speciality, String collage, String profileImage) {
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
                                map.put("certification", certification);
                                map.put("name", nameFull);
                                map.put("email", email);
                                map.put("password", password);
                                map.put("medicalDegree", medicalDegree);
                                map.put("speciality", speciality);
                                map.put("collage", collage);
                                map.put("gender", gender);
                                map.put("image", profileImage);
                                map.put("date", ServerValue.TIMESTAMP);
                                FirebaseDatabase.getInstance().getReference().child("Doctors").child(userid).setValue(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                    String firebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    FirebaseAuth.getInstance().signOut();
                                                    loaderDialog.dismiss();
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(DoctorRegistration.this);
                                                    builder2.setTitle("Registration completed!");
                                                    builder2.setMessage("we have sent you E-mail on " + email + " for verification");
                                                    builder2.setCancelable(false);
                                                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int which) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(DoctorRegistration.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    builder2.show();

                                                } else {
                                                    Log.d(TAG, "registerData failed: " + task.getException().getMessage());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(DoctorRegistration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                base64 = convbase64.replaceAll("\n", "");
                binding.imgProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}