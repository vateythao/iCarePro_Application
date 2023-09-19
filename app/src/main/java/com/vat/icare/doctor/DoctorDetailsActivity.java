package com.vat.icare.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityDoctorDetailsBinding;
import com.vat.icare.medicineAlerts.AddMedicineActivity;

public class DoctorDetailsActivity extends AppCompatActivity {

    ActivityDoctorDetailsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_details);

        binding.btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AddMedicineActivity.class);
                startActivity(intent);
            }
        });
    }
}