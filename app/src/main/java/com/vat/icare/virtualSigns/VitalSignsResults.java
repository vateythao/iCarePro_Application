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
import com.vat.icare.domin.VitalSignViewModel;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.utils.romDB.VitalSign;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VitalSignsResults extends AppCompatActivity {

     String Date, userName;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    int VBP1, VBP2, VRR, VHR, VO2;
    VitalSignViewModel viewModel;

    Context context;
    ActivityVitalSignsResultsBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vital_signs_results);
        viewModel = new ViewModelProvider(this).get(VitalSignViewModel.class);
        context=this;

        Date = df.format(today);
        TextView VSRR = this.findViewById(R.id.RRV);
        TextView VSBPS = this.findViewById(R.id.BP2V);
        TextView VSHR = this.findViewById(R.id.HRV);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            VRR = bundle.getInt("breath");
            VHR = bundle.getInt("bpm");
            VBP1 = bundle.getInt("SP");
            VBP2 = bundle.getInt("DP");
            VO2 = bundle.getInt("O2R");
            userName = bundle.getString("userName");
            VSRR.setText(String.valueOf(VRR));
            VSHR.setText(String.valueOf(VBP2));
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
                int sugar= Integer.parseInt(binding.edtSugar.getText().toString());
                int pulse= Integer.parseInt(binding.RRV.getText().toString());

                VitalSign vitalSign=new VitalSign(userName,VBP1,VBP2,VRR,
                        sugar,pulse);
                viewModel.insertStudent(vitalSign);
                Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
            }
        });
        binding.txtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(VitalSignsResults.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
