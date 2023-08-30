package com.vat.icare.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vat.icare.MainActivity;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityLoginBinding;
import com.vat.icare.registration.RegistrationActivity;
import com.vat.icare.utils.LoaderDialog;
import com.vat.icare.virtualSigns.StartVitalSigns;
import com.vat.icare.virtualSigns.VitalSignsProcess;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "CA/LoginActivity";
    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loaderDialog = new LoaderDialog(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    FirebaseLogin(binding.profileFirstName.getText().toString().trim(),binding.profileLastName.getText().toString().trim());
                }
            }
        });
        binding.signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
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
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loaderDialog.dismiss();
                                Intent intent=new Intent(LoginActivity.this, StartVitalSigns.class);
                                startActivity(intent);
                               //if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                               //
                               //} else {
                               //    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                               //    Toast.makeText(getApplicationContext(), "Your email is not verified, we have sent you a new one.", Toast.LENGTH_LONG).show();
                               //    FirebaseAuth.getInstance().signOut();
                               //}
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