package com.vat.icare.doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vat.icare.R;
import com.vat.icare.appointmentBooking.AppointmentBookingActivity;
import com.vat.icare.calls.VideoCallActivity;
import com.vat.icare.databinding.ActivityDoctorDetailsBinding;

public class DoctorDetailsActivity extends AppCompatActivity {

    ActivityDoctorDetailsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_details);

        String userId = getIntent().getStringExtra("userId");
        String DoctorToken = getIntent().getStringExtra("token");
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String speciality = getIntent().getStringExtra("speciality");
        String about = getIntent().getStringExtra("about");

        byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        binding.imgDoctor.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        binding.txtDocName.setText(name);
        binding.txtDoctorSpec.setText(speciality);
        binding.txtAboutDoc.setText(about);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentBookingActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("token",DoctorToken);
                intent.putExtra("image",image);
                intent.putExtra("name",name);
                intent.putExtra("speciality",speciality);
                startActivity(intent);
            }
        });

        binding.imgVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, VideoCallActivity.class);
                startActivity(intent);
            }
        });
        binding.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorChattingActivity.class);
                intent.putExtra("useridFirebase", userId);
                intent.putExtra("userTokenFirebase", DoctorToken);
                intent.putExtra("userName", name);
                startActivity(intent);
            }
        });
    }
}