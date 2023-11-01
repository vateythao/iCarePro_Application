package com.vat.icare.doctor;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityDoctorMainBinding;
import com.vat.icare.doctor.fragment.BookingFragment;
import com.vat.icare.doctor.fragment.FragmentChatsDoctor;

public class DoctorMainActivity extends AppCompatActivity {
    ActivityDoctorMainBinding binding;
    FirebaseDatabase database;
    FragmentChatsDoctor fragmentChatsDoctor;
    BookingFragment bookingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_main);
        fragmentChatsDoctor = new FragmentChatsDoctor();
        bookingFragment = new BookingFragment();
        setCurrentFragment(fragmentChatsDoctor);
        binding.radioChats.setChecked(true);
        binding.radioChats.setTextColor(getColor(R.color.white));
        binding.radioAppointment.setTextColor(getColor(R.color.black));

        binding.radioTabSelector.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.radioChats) {
                binding.radioChats.setTextColor(getColor(R.color.white));
                binding.radioAppointment.setTextColor(getColor(R.color.black));
                setCurrentFragment(fragmentChatsDoctor);
            }
            if (checkedId == R.id.radioAppointment) {
                binding.radioChats.setTextColor(getColor(R.color.black));
                binding.radioAppointment.setTextColor(getColor(R.color.white));
                setCurrentFragment(bookingFragment);
            }
        });
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainerDoctor, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}