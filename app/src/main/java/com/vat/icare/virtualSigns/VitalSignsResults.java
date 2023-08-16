package com.vat.icare.virtualSigns;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.vat.icare.MainActivity;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityRegistrationBinding;
import com.vat.icare.databinding.ActivityVitalSignsResultsBinding;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.utils.romDB.VitalSign;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VitalSignsResults extends AppCompatActivity {

     String user, Date, userName;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    int VBP1, VBP2, VRR, VHR, VO2;

    Context context;
    ActivityVitalSignsResultsBinding binding;
    LoginViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vital_signs_results);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        context=this;

        Date = df.format(today);
        TextView VSRR = this.findViewById(R.id.RRV);
        TextView VSBPS = this.findViewById(R.id.BP2V);
        TextView VSHR = this.findViewById(R.id.HRV);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            VRR = bundle.getInt("breath");
            userName = bundle.getString("userName");
            VHR = bundle.getInt("bpm");
            VBP1 = bundle.getInt("SP");
            VBP2 = bundle.getInt("DP");
            VSRR.setText(String.valueOf(VRR));
            VSHR.setText(String.valueOf(VHR));
            VSBPS.setText(VBP1 + " / " + VBP2);
        }
        binding.btnStartAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, VitalSignsProcess.class);
                startActivity(intent);
            }
        });
        binding.btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VitalSign vitalSign=new VitalSign(userName,binding.BP2V.getText().toString(),
                        binding.HRV.getText().toString(),binding.edtSugar.getText().toString(),binding.RRV.getText().toString());
                viewModel.insertStudent(vitalSign);
                Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(VitalSignsResults.this, MainActivity.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
    }
}
