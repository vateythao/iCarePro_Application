package com.vat.icare.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vat.icare.R;
import com.vat.icare.chats.fcm.SessionManager;
import com.vat.icare.databinding.ActivityLoginBinding;
import com.vat.icare.doctor.DoctorMainActivity;
import com.vat.icare.main.MainActivity;
import com.vat.icare.registration.RegistrationActivity;
import com.vat.icare.utils.LoaderDialog;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "CA/LoginActivity";
    String doctor="";
    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    LoaderDialog loaderDialog;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loaderDialog = new LoaderDialog(this);

        sessionManager = new SessionManager(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    FirebaseLogin(binding.profileFirstName.getText().toString().trim(),
                            binding.profileLastName.getText().toString().trim());
                }
            }
        });
        binding.rdGSelectionDoc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbNo) {
                    doctor = "No";
                }
                if (checkedId == R.id.rbYes) {
                    doctor = "Yes";
                }
            }
        });
        binding.signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
    public boolean validation(){
        boolean valid=true;
        if(binding.profileFirstName.getText().toString().equals("")){
            valid=false;
            binding.profileFirstName.setError("Email Required");
        } else if (binding.profileLastName.getText().toString().equals("")){
            valid=false;
            binding.profileLastName.setError("Password Required");
        }
        return valid;
    }

    public void FirebaseLogin(String emailId, String passwordId) {
        loaderDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId,passwordId).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(userid).child("token")
                            .setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loaderDialog.dismiss();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(userid);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String type = dataSnapshot.child("type").getValue(String.class);
                                            if(type!=null){
                                                sessionManager.setIsLoginDone(true);
                                                sessionManager.setLoginUserType(type);
                                                if(type.equals("Doctor")){
                                                    Intent intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                                                    startActivity(intent);
                                                }else {
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle the error here
                                    }
                                });
                            } else {
                                Log.d(TAG, "uploadToken failed: " + task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    loaderDialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}