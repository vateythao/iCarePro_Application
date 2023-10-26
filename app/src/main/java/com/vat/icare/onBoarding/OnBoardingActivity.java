package com.vat.icare.onBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vat.icare.R;
import com.vat.icare.chats.fcm.SessionManager;
import com.vat.icare.databinding.ActivityOnBoardrdingBinding;
import com.vat.icare.doctor.DoctorMainActivity;
import com.vat.icare.login.LoginActivity;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.main.MainActivity;
import com.vat.icare.onBoarding.adapter.IntroViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnBoardingActivity extends AppCompatActivity {
    ActivityOnBoardrdingBinding binding;
    LoginViewModel loginViewModel;
    IntroViewPagerAdapter introViewPagerAdapter;
    List<OnBoardingData> onBoardingData = new ArrayList<>();
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boardrding);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loadUser();
    }

    private void loadUser() {
        try {
            SessionManager sessionManager = new SessionManager(OnBoardingActivity.this);
            boolean isLogin = sessionManager.isLoginDone();
            String loginType = sessionManager.loginUserType();
            if (isLogin){
                Intent intent;
                if(loginType.equals("Doctor")){
                    intent = new Intent(OnBoardingActivity.this, DoctorMainActivity.class);
                }else {
                    intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                }
                startActivity(intent);
            }else {
                onBoardingScreenData();
            }
        } catch (Exception e) {
            onBoardingScreenData();
        }
    }

    private void onBoardingScreenData() {
        onBoardingData.add(
                new OnBoardingData(
                        "Let's connection with doctor's around you",
                        "Connect helps you locate doctor's around you who\n are closest from your home.",
                        R.drawable.first
                )
        );
        onBoardingData.add(
                new OnBoardingData(
                        "Anytime,\n Anywhere!",
                        "User will be able to give you live, chat and meet\n with people nearby.",
                        R.drawable.ongoing_second
                )
        );

        setViewPagerAdapter(onBoardingData);

        binding.dotsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setViewPagerAdapter(List<OnBoardingData> onBoardingData) {
        introViewPagerAdapter = new IntroViewPagerAdapter(this, onBoardingData);
        binding.viewPager.setAdapter(introViewPagerAdapter);
        binding.dotsLayout.setupWithViewPager(binding.viewPager);
    }
}