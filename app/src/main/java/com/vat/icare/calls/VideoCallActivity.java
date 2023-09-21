package com.vat.icare.calls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityVideoCallBinding;

public class VideoCallActivity extends AppCompatActivity {
    ActivityVideoCallBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call);
        context=this;
    }
}