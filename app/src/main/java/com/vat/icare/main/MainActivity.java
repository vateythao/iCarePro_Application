package com.vat.icare.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityMainBinding;
import com.vat.icare.main.fragment.BPHeartFragement;
import com.vat.icare.main.fragment.ChatFragment;
import com.vat.icare.main.fragment.DoctorFragment;
import com.vat.icare.main.fragment.ProfileFragment;
import com.vat.icare.virtualSigns.StartVitalSigns;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context;
    DoctorFragment doctors = new DoctorFragment();
    ChatFragment chatFragment = new ChatFragment();
    BPHeartFragement heartFragement = new BPHeartFragement();
    ProfileFragment profileFragment = new ProfileFragment();

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = this;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        binding.actionDoctorMenu.setOnClickListener(view -> {
            setCurrentFragment(doctors);
        });

        binding.actionBpHeart.setOnClickListener(view -> {
            setCurrentFragment(heartFragement);
        });

        binding.actionChat.setOnClickListener(view -> {
            setCurrentFragment(chatFragment);
        });

        binding.actionProfile.setOnClickListener(view -> {
            setCurrentFragment(profileFragment);
        });

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.navDoctor: {
                    setCurrentFragment(doctors);
                    break;
                }
                case R.id.navBPHeart: {
                    setCurrentFragment(heartFragement);
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

    @Override
    protected void onResume() {
        super.onResume();
        doctors = new DoctorFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        setCurrentFragment(doctors);

    }
    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainerFrameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}