package com.vat.icare.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityMainBinding;
import com.vat.icare.main.adapter.DoctorAdapter;
import com.vat.icare.main.fragment.ChatFragment;
import com.vat.icare.main.fragment.DoctorFragment;
import com.vat.icare.main.fragment.ProfileFragment;
import com.vat.icare.virtualSigns.StartVitalSigns;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;


        DoctorFragment doctors = new DoctorFragment();
        ChatFragment chatFragment = new ChatFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        setCurrentFragment(doctors);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.navDoctor: {
                    setCurrentFragment(doctors);
                    break;
                }
                case R.id.navBPHeart: {
                    Intent intent = new Intent(this, StartVitalSigns.class);
                    startActivity(intent);
                    break;
                }
                case R.id.navChats: {
                    setCurrentFragment(chatFragment);
                    break;
                }
                case R.id.navProfile: {
                    setCurrentFragment(profileFragment);
                    break;
                }
            }
            return true;
        });

    }
    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainerFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}